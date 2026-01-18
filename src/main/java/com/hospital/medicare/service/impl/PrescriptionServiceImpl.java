package com.hospital.medicare.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.medicare.dto.PrescriptionDTO;
import com.hospital.medicare.entity.Appointment;
import com.hospital.medicare.entity.Prescription;
import com.hospital.medicare.exception.ResourceNotFoundException;
import com.hospital.medicare.repository.AppointmentRepository;
import com.hospital.medicare.repository.PrescriptionRepository;
import com.hospital.medicare.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

	private final PrescriptionRepository prescriptionRepository;
	private final AppointmentRepository appointmentRepository;

	@Override
	@Transactional
	public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {

		Appointment appointment = appointmentRepository.findById(prescriptionDTO.getAppointmentId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id: " + prescriptionDTO.getAppointmentId()));

		Prescription prescription = Prescription.builder()
				.appointment(appointment)
				.medicineName(prescriptionDTO.getMedicineName())
				.dosage(prescriptionDTO.getDosage())
				.duration(prescriptionDTO.getDuration())
				.instructions(prescriptionDTO.getInstructions())
				.build();

		Prescription savedPrescription = prescriptionRepository.save(prescription);
		return convertToDTO(savedPrescription);
	}

	@Override
	@Transactional
	public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
		Prescription prescription = prescriptionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Prescription not found with id: " + id));
		prescription.setMedicineName(prescriptionDTO.getMedicineName());
		prescription.setDosage(prescriptionDTO.getDosage());
		prescription.setDuration(prescriptionDTO.getDuration());
		prescription.setInstructions(prescriptionDTO.getInstructions());

		Prescription updatedPrescription = prescriptionRepository.save(prescription);
		return convertToDTO(updatedPrescription);
	}

	@Override
	@Transactional(readOnly = true)
	public PrescriptionDTO getPrescriptionById(Long id) {
		Prescription prescription = prescriptionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Prescription not found with id: " + id));
		return convertToDTO(prescription);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PrescriptionDTO> getPrescriptionsByAppointment(Long appointmentId) {
		return prescriptionRepository.findByAppointmentId(appointmentId)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<PrescriptionDTO> getPrescriptionsByPatient(Long patientId) {
		return prescriptionRepository.findByPatientId(patientId)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<PrescriptionDTO> getPrescriptionsByDoctor(Long doctorId) {
		return prescriptionRepository.findByDoctorId(doctorId)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deletePrescription(Long id) {
		if (!prescriptionRepository.existsById(id)) {
			throw new ResourceNotFoundException("Prescription not found with id: " + id);
		}
		prescriptionRepository.deleteById(id);
	}

	private PrescriptionDTO convertToDTO(Prescription prescription) {
		return PrescriptionDTO.builder()
				.id(prescription.getId())
				.appointmentId(prescription.getAppointment()
						.getId())
				.medicineName(prescription.getMedicineName())
				.dosage(prescription.getDosage())
				.duration(prescription.getDuration())
				.instructions(prescription.getInstructions())
				.patientName(prescription.getAppointment()
						.getPatient()
						.getUser()
						.getFullName())
				.doctorName(prescription.getAppointment()
						.getDoctor()
						.getUser()
						.getFullName())
				.build();

	}
}
