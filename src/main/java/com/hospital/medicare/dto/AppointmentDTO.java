package com.hospital.medicare.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Future;
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
public class AppointmentDTO {

	private Long id;

	private Long patientId;

	private String patientName;
	private String patientEmail;

	@NotNull(message = "doctor is required")
	private Long doctorId;

	private String doctorName;
	private String doctorSpecialization;

	@NotNull(message = "Appointment date is required")
	@Future(message = "Appointment date must be in the future")
	private LocalDate appointmentDate;

	@NotNull(message = "Appointment time is required")
	private LocalTime appointmentTime;

	private String status;

	@Size(max = 500, message = "Symptoms description cannot exceed 500 characters")
	private String symptoms;

	@Size(max = 1000, message = "Diagnosis cannot exceed 1000 characters")
	private String diagnosis;

	private String departmentName;

}
