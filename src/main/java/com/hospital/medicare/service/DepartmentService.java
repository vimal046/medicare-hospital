package com.hospital.medicare.service;

import java.util.List;

import com.hospital.medicare.dto.DepartmentDTO;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
	DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
	DepartmentDTO getDepartmentById(Long id);
	List<DepartmentDTO> getAllDepartments();
	void deleteDepartment(Long id);
}
