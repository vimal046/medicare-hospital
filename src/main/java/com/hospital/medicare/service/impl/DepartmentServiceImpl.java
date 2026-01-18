package com.hospital.medicare.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.medicare.dto.DepartmentDTO;
import com.hospital.medicare.entity.Department;
import com.hospital.medicare.exception.DuplicateResourceException;
import com.hospital.medicare.exception.ResourceNotFoundException;
import com.hospital.medicare.repository.DepartmentRepository;
import com.hospital.medicare.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	@Override
	@Transactional
	public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {

		if (departmentRepository.existsByName(departmentDTO.getName())) {
			throw new DuplicateResourceException(
					"Department already exists with name: " + departmentDTO.getName());
		}

		Department department = Department.builder()
				.name(departmentDTO.getName())
				.description(departmentDTO.getDescription())
				.build();
		Department savedDepartment = departmentRepository.save(department);
		return convertToDTO(savedDepartment);
	}

	@Override
	@Transactional
	public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Department not found with id: " + id));

		if (!department.getName()
				.equals(departmentDTO.getName())
				&& departmentRepository.existsByName(departmentDTO.getName())) {
			throw new DuplicateResourceException(
					"Department already exists with the name: " + departmentDTO.getName());
		}

		department.setName(departmentDTO.getName());
		department.setDescription(departmentDTO.getDescription());

		Department updatedDepartment = departmentRepository.save(department);

		return convertToDTO(updatedDepartment);
	}

	@Override
	@Transactional(readOnly = true)
	public DepartmentDTO getDepartmentById(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Department not found with id: " + id));
		return convertToDTO(department);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DepartmentDTO> getAllDepartments() {
		return departmentRepository.findAll()
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteDepartment(Long id) {
		if (!departmentRepository.existsById(id)) {
			throw new ResourceNotFoundException("Department not found with the id: " + id);
		}
		departmentRepository.deleteById(id);
	}

	private DepartmentDTO convertToDTO(Department department) {
		return DepartmentDTO.builder()
				.id(department.getId())
				.name(department.getName())
				.description(department.getDescription())
				.doctorCount(department.getDoctors() != null ? department.getDoctors()
						.size() : 0)
				.build();
	}
}
