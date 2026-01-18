package com.hospital.medicare.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleResourceNotFoundException(ResourceNotFoundException ex,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", ex.getMessage());
		return "redirect:/";
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public String handleDuplicateResourceException(DuplicateResourceException ex,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", ex.getMessage());
		return "redirect:/register";
	}

	public String handleValidationException(MethodArgumentNotValidException ex, Model model) {

		String errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + " : " + error.getDefaultMessage())
				.collect(Collectors.joining(" , "));

		model.addAttribute("error", "Validation Failed: " + errors);
		return "register";
	}

	public String handleGenericException(Exception ex, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error", "An error occured: " + ex.getMessage());
		return "redirect:/";
	}
}
