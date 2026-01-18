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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.medicare.dto.PatientDTO;
import com.hospital.medicare.security.CustomUserDetails;
import com.hospital.medicare.service.AppointmentService;
import com.hospital.medicare.service.PatientService;
import com.hospital.medicare.service.PrescriptionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

	private final PatientService patientService;
	private final AppointmentService appointmentService;
	private final PrescriptionService prescriptionService;

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model,
			HttpServletRequest request) {

		model.addAttribute("currentUri", request.getRequestURI());

		try {
			PatientDTO patient = patientService.getPatientByUserId(userDetails.getUserId());
			model.addAttribute("patient", patient);
			model.addAttribute("appointments",
					appointmentService.getAppointmentsByPatient(patient.getId()));
			return "patient/dashboard";
		} catch (Exception e) {

			model.addAttribute("patient", new PatientDTO());
			model.addAttribute("message", "Please complete your profile");
			return "patient/complete-profile";
		}
	}

	@PostMapping("/profile/complete")
	public String completeProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
			@Valid @ModelAttribute("patient") PatientDTO patientDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model) {

		if (result.hasErrors()) {
			return "patient/complete-profile";
		}

		try {
			patientDTO.setUserId(userDetails.getUserId());
			patientDTO.setFullName(userDetails.getFullName());
			patientDTO.setEmail(userDetails.getUsername());
			patientService.createPatient(patientDTO);
			redirectAttributes.addFlashAttribute("success", "Profile completed successfully");
			return "redirect:/patient/dashboard";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "patient/complete-profile";
		}

	}

	@GetMapping("/appointments")
	public String viewAppointments(@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model,
			HttpServletRequest request) {
		model.addAttribute("currentUri", request.getRequestURI());
		try {
			PatientDTO patient = patientService.getPatientByUserId(userDetails.getUserId());
			model.addAttribute("appointments",
					appointmentService.getAppointmentsByPatient(patient.getId()));
			return "patient/appointments";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@GetMapping("/prescriptions")
	public String viewPrescriptions(@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model,
			HttpServletRequest request) {
		model.addAttribute("currentUri", request.getRequestURI());
		try {
			PatientDTO patient = patientService.getPatientByUserId(userDetails.getUserId());
			model.addAttribute("prescriptions",
					prescriptionService.getPrescriptionsByPatient(patient.getId()));
			return "patient/prescriptions";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/appointments/{id}/cancel")
	public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		try {
			appointmentService.cancelAppointment(id);
			redirectAttributes.addFlashAttribute("success", "Appointment cancelled successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/patient/appointments";
	}
}
