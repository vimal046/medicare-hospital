package com.hospital.medicare.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.medicare.dto.DoctorDTO;
import com.hospital.medicare.dto.PrescriptionDTO;
import com.hospital.medicare.security.CustomUserDetails;
import com.hospital.medicare.service.AppointmentService;
import com.hospital.medicare.service.DoctorService;
import com.hospital.medicare.service.PrescriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

	private final DoctorService doctorService;
	private final AppointmentService appointmentService;
	private final PrescriptionService prescriptionService;

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		try {
			DoctorDTO doctor = doctorService.getDoctorById(userDetails.getUserId());
			model.addAttribute("doctor", doctor);
			model.addAttribute("appointments",
					appointmentService.getAppointmentsByDoctor(doctor.getId()));
			return "doctor/dashboard";
		} catch (Exception e) {
			model.addAttribute("error", "Doctor profile not found. Please contact administrator");
			return "error";
		}
	}

	@GetMapping("/appointments")
	public String viewAppointments(@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {
		try {
			DoctorDTO doctor = doctorService.getDoctorByUserId(userDetails.getUserId());
			model.addAttribute("appointments",
					appointmentService.getAppointmentsByDoctor(doctor.getId()));

			return "doctor/appointments";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@GetMapping("/appointments/{id}")
	public String viewAppointmentDetails(@PathVariable Long id, Model model) {
		model.addAttribute("appointment", appointmentService.getAppointmentById(id));
		model.addAttribute("prescriptions", prescriptionService.getPrescriptionsByAppointment(id));
		model.addAttribute("prescription", new PrescriptionDTO());
		return "doctor/appointment-details";
	}

	@PostMapping("/appointments/{id}/complete")
	public String completeAppointment(@PathVariable Long id,
			@RequestParam String diagnosis,
			RedirectAttributes redirectAttributes) {
		try {
			appointmentService.completeAppointment(id, diagnosis);
			redirectAttributes.addFlashAttribute("success", "Appointment completed successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/doctor/appointments/" + id;
	}

	@PostMapping("/prescriptions/add")
	public String addPrescription(
			@Valid @ModelAttribute("prescription") PrescriptionDTO prescriptionDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Validation failed");
			return "redirect:/doctor/appointments/" + prescriptionDTO.getAppointmentId();
		}

		try {
			prescriptionService.createPrescription(prescriptionDTO);
			redirectAttributes.addFlashAttribute("success", "Prescription added successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/doctor/appointments/" + prescriptionDTO.getAppointmentId();
	}

	@GetMapping("/patient-history/{patientId}")
	public String viewPatientHistory(@PathVariable Long patientId, Model model) {
		model.addAttribute("appointments", appointmentService.getAppointmentsByPatient(patientId));
		model.addAttribute("prescriptions",
				prescriptionService.getPrescriptionsByPatient(patientId));
		return "doctor/patient-history";
	}
}
