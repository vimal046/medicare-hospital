package com.hospital.medicare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.medicare.dto.DepartmentDTO;
import com.hospital.medicare.dto.DoctorDTO;
import com.hospital.medicare.dto.UserRegistrationDTO;
import com.hospital.medicare.entity.User;
import com.hospital.medicare.service.AppointmentService;
import com.hospital.medicare.service.DepartmentService;
import com.hospital.medicare.service.DoctorService;
import com.hospital.medicare.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;
	private final DoctorService doctorService;
	private final DepartmentService departmentService;
	private final AppointmentService appointmentService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("doctors", doctorService.getAllDoctors());
		model.addAttribute("departments", departmentService.getAllDepartments());
		model.addAttribute("appointments", appointmentService.getAllAppointments());
		return "admin/dashboard";
	}

	// =====Department Management=====

	@GetMapping("/departments")
	public String manageDepartments(Model model) {
		model.addAttribute("departments", departmentService.getAllDepartments());
		model.addAttribute("department", new DepartmentDTO());
		return "admin/manage-departments";
	}

	@PostMapping("/departments/add")
	public String addDepartment(@Valid @ModelAttribute("department") DepartmentDTO departmentDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("departments", departmentService.getAllDepartments());
			return "admin/manage-departments";
		}
		try {
			departmentService.createDepartment(departmentDTO);
			redirectAttributes.addFlashAttribute("success", "Department added successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/admin/departments";
	}

	@PostMapping("/departments/delete/{id}")
	public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			departmentService.deleteDepartment(id);
			redirectAttributes.addFlashAttribute("success", "Department deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/admin/departments";
	}

	// Doctor Management
	@GetMapping("/doctors")
	public String manageDoctors(Model model) {
		model.addAttribute("doctors", doctorService.getAllDoctors());
		return "admin/manage-doctors";
	}

	@GetMapping("/doctors/add")
	public String addDoctorForm(Model model) {
		model.addAttribute("doctor", new DoctorDTO());
		model.addAttribute("user", new UserRegistrationDTO());
		model.addAttribute("departments", departmentService.getAllDepartments());
		return "admin/add-doctor";
	}

	@PostMapping("/doctors/add")
	public String addDoctor(@Valid @ModelAttribute("user") UserRegistrationDTO userDTO,
			BindingResult userResult,
			@Valid @ModelAttribute("doctor") DoctorDTO doctorDTO,
			BindingResult doctorResult,
			RedirectAttributes redirectAttributes,
			Model model) {

		if (userResult.hasErrors() || doctorResult.hasErrors()) {
			model.addAttribute("departments", departmentService.getAllDepartments());
			return "admin/add-doctor";
		}

		try {
			// Register user with DOCTOR role
			userDTO.setRole("DOCTOR");
			User savedUser = userService.registerUser(userDTO);

			// Create doctor profile
			doctorDTO.setUserId(savedUser.getId());
			doctorDTO.setFullName(savedUser.getFullName());
			doctorDTO.setEmail(savedUser.getEmail());
			doctorDTO.setPhone(savedUser.getPhone());
			doctorService.createDoctor(doctorDTO);

			redirectAttributes.addFlashAttribute("success", "Doctor added successfully");
			return "redirect:/admin/doctors";

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("departments", departmentService.getAllDepartments());
			return "admin/add-doctor";
		}
	}

	@PostMapping("/doctors/delete/{id}")
	public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			doctorService.deleteDoctor(id);
			redirectAttributes.addFlashAttribute("success", "Doctor deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/admin/doctors";
	}

	// Appointment Management
	@GetMapping("/appointments")
	public String viewAllAppointments(Model model) {
		model.addAttribute("appointments", appointmentService.getAllAppointments());
		return "admin/all-appointments";
	}
}
