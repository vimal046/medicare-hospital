package com.hospital.medicare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {

	private Long id;
	private Long userId;

	@NotBlank(message = "Full name is required")
	private String fullName;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provice a valid email address")
	private String email;

	@NotBlank(message = "Phone is required")
	private String phone;

	@NotBlank(message = "Specialization is required")
	private String specialization;

	@NotBlank(message = "License number is required")
	private String licenseNumber;

	@NotNull(message = "Experience is required")
	@Min(value = 0, message = "Experience cannot be negative")
	private Integer experience;

	private String qualifications;

	@NotNull(message = "Department is required")
	private Long departmentId;

	private String departmentName;
}
