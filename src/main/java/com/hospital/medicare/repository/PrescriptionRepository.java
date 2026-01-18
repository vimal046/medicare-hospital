package com.hospital.medicare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.medicare.entity.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	List<Prescription> findByAppointmentId(Long appointmentId);

	@Query("SELECT p FROM Prescription p WHERE p.appointment.patient.id = :patientId ORDER BY p.createdAt DESC")
	List<Prescription> findByPatientId(@Param("patientId") Long patientId);

	@Query("SELECT p FROM Prescription p WHERE p.appointment.doctor.id = :doctorId ORDER BY p.createdAt DESC")
	List<Prescription> findByDoctorId(@Param("doctorId") Long doctorId);
}
