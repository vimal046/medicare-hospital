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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.medicare.dto.AppointmentDTO;
import com.hospital.medicare.dto.PatientDTO;
import com.hospital.medicare.security.CustomUserDetails;
import com.hospital.medicare.service.AppointmentService;
import com.hospital.medicare.service.DepartmentService;
import com.hospital.medicare.service.DoctorService;
import com.hospital.medicare.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/patient/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;
	private final DoctorService doctorService;
	private final DepartmentService departmentService;
	private final PatientService patientService;

	@GetMapping("/book")
	public String bookAppointmentForm(@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {
		try {
			PatientDTO patient = patientService.getPatientByUserId(userDetails.getUserId());
			model.addAttribute("appointment", new AppointmentDTO());
			model.addAttribute("departments", departmentService.getAllDepartments());
			model.addAttribute("doctors", doctorService.getAllDoctors());
			model.addAttribute("patientId", patient.getId());
			return "patient/book-appointment";
		} catch (Exception e) {
			model.addAttribute("error", "Please complete your profile first");
			return "redirect:/patient/dashboard";
		}
	}

	@GetMapping("/doctors-by-department/{departmentId}")
	@ResponseBody
	public String getDoctorByDepartment(@PathVariable Long departmentId) {
		return doctorService.getDoctorsByDepartment(departmentId)
				.toString();
	}

	@PostMapping("/book")
	public String bookAppointment(@AuthenticationPrincipal CustomUserDetails userDetails,
			@Valid @ModelAttribute("appointment") AppointmentDTO appointmentDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model) {
		if (result.hasErrors()) {

			result.getFieldErrors()
					.forEach(error -> {
						System.out.println("Field: " + error.getField() + " | Rejected value: "
								+ error.getRejectedValue() + " | Message: "
								+ error.getDefaultMessage());
					});

			model.addAttribute("departments", departmentService.getAllDepartments());
			model.addAttribute("doctors", doctorService.getAllDoctors());
			return "patient/book-appointment";
		}

		try {
			PatientDTO patient = patientService.getPatientByUserId(userDetails.getUserId());
			appointmentDTO.setPatientId(patient.getId());
			appointmentService.bookAppointment(appointmentDTO);
			redirectAttributes.addFlashAttribute("success", "Appointment booked successfully");
			return "redirect:/patient/appointments";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("departments", appointmentService.getAllAppointments());
			model.addAttribute("doctors", doctorService.getAllDoctors());
			return "patient/book-appointment";
		}
	}

}
