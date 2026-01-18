package com.hospital.medicare.service;

import java.util.List;

import com.hospital.medicare.dto.PrescriptionDTO;

public interface PrescriptionService {

	PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO);
	PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO);
	PrescriptionDTO getPrescriptionById(Long id);
	List<PrescriptionDTO> getPrescriptionsByAppointment(Long appointmentId);
	List<PrescriptionDTO> getPrescriptionsByPatient(Long patientId);
	List<PrescriptionDTO> getPrescriptionsByDoctor(Long doctorId);
	void deletePrescription(Long id);
}
