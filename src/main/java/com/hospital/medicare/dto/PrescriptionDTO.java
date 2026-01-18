package com.hospital.medicare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionDTO {

	private Long id;

	@NotNull(message = "Appointment is required")
	private Long appointmentId;

	@NotBlank(message = "Medicine name is required")
	@Size(max = 200, message = "Medicine name cannot exceed 200 characters")
	private String medicineName;

	@NotBlank(message = "Dosage is required")
	@Size(max = 100, message = "Dosage cannot exceed 100 characters")
	private String dosage;

	@NotNull(message = "Duration is required")
	@Min(value = 1, message = "Duration must be atleast 1 day")
	private Integer duration;

	@Size(max = 500, message = "Instructions cannot exceed 500 characters")
	private String instructions;

	private String patientName;
	private String doctorName;
}
