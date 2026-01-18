package com.hospital.medicare.service;

import java.util.List;

import com.hospital.medicare.dto.DoctorDTO;

public interface DoctorService {

	DoctorDTO createDoctor(DoctorDTO doctorDTO);
	DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO);
	DoctorDTO getDoctorById(Long id);
	DoctorDTO getDoctorByUserId(Long userId);
	List<DoctorDTO> getAllDoctors();
	List<DoctorDTO> getDoctorsByDepartment(Long departmentId);
	void deleteDoctor(Long id);
}
