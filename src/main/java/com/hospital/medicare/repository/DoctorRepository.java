package com.hospital.medicare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hospital.medicare.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	Optional<Doctor> findByUserId(Long userId);

	Optional<Doctor> findByLicenseNumber(Long licenseNumber);

	List<Doctor> findByDepartmentId(Long departmentId);

	Boolean existsByLicenseNumber(String licenseNumber);

	@Query("SELECT d FROM Doctor d JOIN FETCH d.user JOIN FETCH d.department")
	List<Doctor> findAllWithDetails();
}
