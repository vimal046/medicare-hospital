package com.hospital.medicare.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.medicare.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPatientId(Long patientId);
	List<Appointment> findByDoctorId(Long doctorId);
	List<Appointment> findByStatus(String status);

	@Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = :status")
	List<Appointment> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId,
			@Param("status") String status);

	@Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
	List<Appointment> findByPatientIdAndStatus(@Param("patientId") Long patientId,
			@Param("status") String status);

	@Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date")
	List<Appointment> findByDoctorIdAndDate(@Param("doctorId") Long doctorId,
			@Param("date") LocalDate date);

	@Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor ORDER BY a.appointmentDate DESC, a.appointmentTime DESC")
	List<Appointment> findAllWithDetails();
}
