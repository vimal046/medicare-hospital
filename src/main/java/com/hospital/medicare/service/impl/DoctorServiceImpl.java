package com.hospital.medicare.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.medicare.dto.DoctorDTO;
import com.hospital.medicare.entity.Department;
import com.hospital.medicare.entity.Doctor;
import com.hospital.medicare.entity.User;
import com.hospital.medicare.exception.ResourceNotFoundException;
import com.hospital.medicare.repository.DepartmentRepository;
import com.hospital.medicare.repository.DoctorRepository;
import com.hospital.medicare.repository.UserRepository;
import com.hospital.medicare.service.DoctorService;
import com.sun.jdi.request.DuplicateRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;
	private final DepartmentRepository departmentRepository;

	@Override
	@Transactional
	public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
		if (doctorRepository.existsByLicenseNumber(doctorDTO.getLicenseNumber())) {
			throw new DuplicateRequestException(
					"Doctor already exists with license number: " + doctorDTO.getLicenseNumber());
		}

		User user = userRepository.findById(doctorDTO.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + doctorDTO.getUserId()));

		Department department = departmentRepository.findById(doctorDTO.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Department not found with the id: " + doctorDTO.getDepartmentId()));

		Doctor doctor = Doctor.builder()
				.user(user)
				.specialization(doctorDTO.getSpecialization())
				.licenseNumber(doctorDTO.getLicenseNumber())
				.experience(doctorDTO.getExperience())
				.qualifications(doctorDTO.getQualifications())
				.department(department)
				.build();

		Doctor savedDoctor = doctorRepository.save(doctor);

		return convertToDTO(savedDoctor);
	}

	@Override
	@Transactional
	public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Doctor not found with id: " + id));

		if (!doctor.getLicenseNumber()
				.equals(doctorDTO.getLicenseNumber())
				&& doctorRepository.existsByLicenseNumber(doctorDTO.getLicenseNumber())) {
			new DuplicateRequestException(
					"Doctor already exists with license number: " + doctorDTO.getLicenseNumber());
		}

		Department department = departmentRepository.findById(doctorDTO.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Department not found with id: " + doctorDTO.getDepartmentId()));

		doctor.setSpecialization(doctorDTO.getSpecialization());
		doctor.setLicenseNumber(doctorDTO.getLicenseNumber());
		doctor.setExperience(doctorDTO.getExperience());
		doctor.setQualifications(doctorDTO.getQualifications());
		doctor.setDepartment(department);

		Doctor updatedDoctor = doctorRepository.save(doctor);
		return convertToDTO(updatedDoctor);
	}

	@Override
	@Transactional(readOnly = true)
	public DoctorDTO getDoctorById(Long id) {
		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Doctor not found with id: " + id));
		return convertToDTO(doctor);
	}

	@Override
	@Transactional(readOnly = true)
	public DoctorDTO getDoctorByUserId(Long userId) {
		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Doctor not found for user id: " + userId));
		return convertToDTO(doctor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DoctorDTO> getAllDoctors() {
		return doctorRepository.findAll()
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DoctorDTO> getDoctorsByDepartment(Long departmentId) {
		return doctorRepository.findByDepartmentId(departmentId)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteDoctor(Long id) {
		if (!doctorRepository.existsById(id)) {
			throw new ResourceNotFoundException("Doctor not found with id: " + id);
		}
		doctorRepository.deleteById(id);
	}

	private DoctorDTO convertToDTO(Doctor doctor) {
		return DoctorDTO.builder()
				.id(doctor.getId())
				.userId(doctor.getUser()
						.getId())
				.fullName(doctor.getUser()
						.getFullName())
				.email(doctor.getUser()
						.getEmail())
				.phone(doctor.getUser()
						.getPhone())
				.specialization(doctor.getSpecialization())
				.licenseNumber(doctor.getLicenseNumber())
				.experience(doctor.getExperience())
				.qualifications(doctor.getQualifications())
				.departmentId(doctor.getDepartment()
						.getId())
				.departmentName(doctor.getDepartment()
						.getName())
				.build();
	}
}
