package com.hospital.medicare.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.medicare.dto.AppointmentDTO;
import com.hospital.medicare.entity.Appointment;
import com.hospital.medicare.entity.Doctor;
import com.hospital.medicare.entity.Patient;
import com.hospital.medicare.exception.ResourceNotFoundException;
import com.hospital.medicare.repository.AppointmentRepository;
import com.hospital.medicare.repository.DoctorRepository;
import com.hospital.medicare.repository.PatientRepository;
import com.hospital.medicare.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;
	private final DoctorRepository doctorRepository;

	@Override
	@Transactional
	public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO) {
		Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Patient not found with id: " + appointmentDTO.getPatientId()));

		Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Doctor not found with id: " + appointmentDTO.getDoctorId()));

		Appointment appointment = Appointment.builder()
				.patient(patient)
				.doctor(doctor)
				.appointmentDate(appointmentDTO.getAppointmentDate())
				.appointmentTime(appointmentDTO.getAppointmentTime())
				.symptoms(appointmentDTO.getSymptoms())
				.status("SCHEDULED")
				.build();

		Appointment savedAppointment = appointmentRepository.save(appointment);
		return convertToDTO(savedAppointment);
	}

	@Override
	@Transactional
	public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id: " + id));

		if (appointmentDTO.getAppointmentDate() != null) {
			appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
		}

		if (appointmentDTO.getAppointmentTime() != null) {
			appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
		}

		if (appointmentDTO.getSymptoms() != null) {
			appointment.setSymptoms(appointmentDTO.getSymptoms());
		}

		if (appointmentDTO.getDiagnosis() != null) {
			appointment.setDiagnosis(appointmentDTO.getDiagnosis());
		}

		if (appointmentDTO.getStatus() != null) {
			appointment.setStatus(appointmentDTO.getStatus());
		}

		Appointment updatAppointment = appointmentRepository.save(appointment);
		return convertToDTO(updatAppointment);
	}

	@Override
	@Transactional(readOnly = true)
	public AppointmentDTO getAppointmentById(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id: " + id));
		return convertToDTO(appointment);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentDTO> getAllAppointments() {
		return appointmentRepository.findAll()
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentDTO> getAppointmentsByPatient(Long patientId) {
		return appointmentRepository.findByPatientId(patientId)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId) {
		return appointmentRepository.findByDoctorId(doctorId)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentDTO> getAppointmentsByStatus(String status) {
		return appointmentRepository.findByStatus(status)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public void cancelAppointment(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id: " + id));
		appointment.setStatus("CANCELLED");
		appointmentRepository.save(appointment);

	}

	@Override
	public void completeAppointment(Long id, String diagnosis) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id: " + id));
		appointment.setStatus("COMPLETED");
		appointment.setDiagnosis(diagnosis);
		appointmentRepository.save(appointment);
	}

	private AppointmentDTO convertToDTO(Appointment appointment) {
		return AppointmentDTO.builder()
				.id(appointment.getId())
				.patientId(appointment.getPatient()
						.getId())
				.patientName(appointment.getPatient()
						.getUser()
						.getFullName())
				.patientEmail(appointment.getPatient()
						.getUser()
						.getEmail())
				.doctorId(appointment.getDoctor()
						.getId())
				.doctorName(appointment.getDoctor()
						.getUser()
						.getFullName())
				.doctorSpecialization(appointment.getDoctor()
						.getSpecialization())
				.appointmentDate(appointment.getAppointmentDate())
				.appointmentTime(appointment.getAppointmentTime())
				.status(appointment.getStatus())
				.symptoms(appointment.getSymptoms())
				.diagnosis(appointment.getDiagnosis())
				.departmentName(appointment.getDoctor()
						.getDepartment()
						.getName())
				.build();
	}

}
