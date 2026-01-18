package com.hospital.medicare.service;

import java.util.List;

import com.hospital.medicare.dto.AppointmentDTO;

public interface AppointmentService {

	AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO);
	AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);
	AppointmentDTO getAppointmentById(Long id);
	List<AppointmentDTO> getAllAppointments();
	List<AppointmentDTO> getAppointmentsByPatient(Long patientId);
	List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId);
	List<AppointmentDTO> getAppointmentsByStatus(String status);
	void cancelAppointment(Long id);
	void completeAppointment(Long id, String diagnosis);
}
