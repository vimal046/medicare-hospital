package com.hospital.medicare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.medicare.dto.UserRegistrationDTO;
import com.hospital.medicare.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@GetMapping("/login")
	public String loginPage(@RequestParam(required = false) String error,
			@RequestParam(required = false) String logout,
			Model model) {
		if (error != null) {
			model.addAttribute("error", "Invalid username or password");
		}
		if (logout != null) {
			model.addAttribute("message", "You have been logged out successfully");
		}
		return "login";
	}

	@GetMapping("/register")
	public String registerationPage(Model model) {
		model.addAttribute("user", new UserRegistrationDTO());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model) {

		if (result.hasErrors()) {
			return "register";
		}

		try {
			// Register user with PATIENT role by default
			registrationDTO.setRole("PATIENT");
			userService.registerUser(registrationDTO);

			redirectAttributes.addFlashAttribute("success",
					"Registration successful! Please login and complete your profile.");
			return "redirect:/login";

		} catch (Exception e) {
			model.addAttribute("error", "Registration failed: " + e.getMessage());
			return "register";
		}
	}
}
