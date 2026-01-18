package com.hospital.medicare.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.medicare.dto.PatientDTO;
import com.hospital.medicare.entity.Patient;
import com.hospital.medicare.entity.User;
import com.hospital.medicare.exception.ResourceNotFoundException;
import com.hospital.medicare.repository.PatientRepository;
import com.hospital.medicare.repository.UserRepository;
import com.hospital.medicare.service.PatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public PatientDTO createPatient(PatientDTO patientDTO) {

		User user = userRepository.findById(patientDTO.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + patientDTO.getUserId()));

		Patient patient = Patient.builder()
				.user(user)
				.dateOfBirth(patientDTO.getDateOfBirth())
				.gender(patientDTO.getGender())
				.bloodGroup(patientDTO.getBloodGroup())
				.address(patientDTO.getAddress())
				.medicalHistory(patientDTO.getMedicalHistory())
				.build();

		Patient savedPatient = patientRepository.save(patient);
		return convertToDTO(savedPatient);
	}

	@Override
	@Transactional
	public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Patient not found with id: " + id));

		patient.setDateOfBirth(patientDTO.getDateOfBirth());
		patient.setGender(patientDTO.getGender());
		patient.setBloodGroup(patientDTO.getBloodGroup());
		patient.setAddress(patientDTO.getAddress());
		patient.setMedicalHistory(patientDTO.getMedicalHistory());

		Patient updatedPatient = patientRepository.save(patient);
		return convertToDTO(updatedPatient);
	}

	@Override
	@Transactional(readOnly = true)
	public PatientDTO getPatientById(Long id) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Patient not found with id: " + id));
		return convertToDTO(patient);
	}

	@Override
	@Transactional(readOnly = true)
	public PatientDTO getPatientByUserId(Long userId) {
		Patient patient = patientRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Patient not found for the user id: " + userId));
		return convertToDTO(patient);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientDTO> getAllPatients() {

		return patientRepository.findAll()
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deletePatient(Long id) {
		if (!patientRepository.existsById(id)) {
			throw new ResourceNotFoundException("Patient not found with id: " + id);
		}
		patientRepository.deleteById(id);
	}

	private PatientDTO convertToDTO(Patient patient) {

		return PatientDTO.builder()
				.id(patient.getId())
				.userId(patient.getUser()
						.getId())
				.fullName(patient.getUser()
						.getFullName())
				.email(patient.getUser()
						.getEmail())
				.phone(patient.getUser()
						.getPhone())
				.dateOfBirth(patient.getDateOfBirth())
				.gender(patient.getGender())
				.bloodGroup(patient.getBloodGroup())
				.address(patient.getAddress())
				.medicalHistory(patient.getMedicalHistory())
				.build();
	}

}
