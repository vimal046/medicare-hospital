package com.hospital.medicare.service;

import java.util.List;

import com.hospital.medicare.dto.PatientDTO;

public interface PatientService {

	PatientDTO createPatient(PatientDTO patientDTO);
	PatientDTO updatePatient(Long id, PatientDTO patientDTO);
	PatientDTO getPatientById(Long id);
	PatientDTO getPatientByUserId(Long userId);
	List<PatientDTO> getAllPatients();
	void deletePatient(Long id);
}
