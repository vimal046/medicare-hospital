package com.hospital.medicare.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {

	private Long id;
	private Long userId;

	private String fullName;

	private String email;

	@NotBlank(message = "Phone is required")
	private String phone;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	private LocalDate dateOfBirth;

	@NotBlank(message = "Gender is required")
	private String gender;

	private String bloodGroup;
	private String address;
	private String medicalHistory;
}
