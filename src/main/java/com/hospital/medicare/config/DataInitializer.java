package com.hospital.medicare.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hospital.medicare.entity.Appointment;
import com.hospital.medicare.entity.Department;
import com.hospital.medicare.entity.Doctor;
import com.hospital.medicare.entity.Patient;
import com.hospital.medicare.entity.Prescription;
import com.hospital.medicare.entity.Role;
import com.hospital.medicare.entity.User;
import com.hospital.medicare.repository.AppointmentRepository;
import com.hospital.medicare.repository.DepartmentRepository;
import com.hospital.medicare.repository.DoctorRepository;
import com.hospital.medicare.repository.PatientRepository;
import com.hospital.medicare.repository.PrescriptionRepository;
import com.hospital.medicare.repository.RoleRepository;
import com.hospital.medicare.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final DepartmentRepository departmentRepository;
	private final DoctorRepository doctorRepository;
	private final PatientRepository patientRepository;
	private final AppointmentRepository appointmentRepository;
	private final PrescriptionRepository prescriptionRepository;
	private final PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initializeData() {
		return args -> {
			// Check if data already exists
			if (roleRepository.count() > 0) {
				log.info("✓ Database already initialized. Skipping data initialization.");
				return;
			}

			log.info("🚀 Starting database initialization...");

			// Step 1: Initialize Roles
			initializeRoles();

			// Step 2: Initialize Departments
			initializeDepartments();

			// Step 3: Initialize Admin User
			initializeAdmin();

			// Step 4: Initialize Doctors
			initializeDoctors();

			// Step 5: Initialize Patients
			initializePatients();

			// Step 6: Initialize Sample Appointments
			initializeAppointments();

			// Step 7: Initialize Sample Prescriptions
			initializePrescriptions();

			log.info("✅ Database initialization completed successfully!");
			printCredentials();
		};
	}

	private void initializeRoles() {
		log.info("📝 Creating roles...");

		Role adminRole = Role.builder()
				.name("ADMIN")
				.build();
		Role doctorRole = Role.builder()
				.name("DOCTOR")
				.build();
		Role patientRole = Role.builder()
				.name("PATIENT")
				.build();

		roleRepository.save(adminRole);
		roleRepository.save(doctorRole);
		roleRepository.save(patientRole);

		log.info("✓ Roles created: ADMIN, DOCTOR, PATIENT");
	}

	private void initializeDepartments() {
		log.info("🏥 Creating departments...");

		String[][] departments = { { "Cardiology", "Heart and cardiovascular system care" },
				{ "Neurology", "Brain and nervous system treatment" },
				{ "Orthopedics", "Bone, joint, and muscle care" },
				{ "Pediatrics", "Child healthcare and development" },
				{ "Dermatology", "Skin, hair, and nail treatment" },
				{ "Ophthalmology", "Eye care and vision treatment" },
				{ "ENT", "Ear, Nose, and Throat specialist care" },
				{ "General Medicine", "Primary healthcare and general consultation" },
				{ "Gynecology", "Women's health and reproductive care" },
				{ "Psychiatry", "Mental health and psychological care" } };

		for (String[] dept : departments) {
			Department department = Department.builder()
					.name(dept[0])
					.description(dept[1])
					.build();
			departmentRepository.save(department);
		}

		log.info("✓ Created {} departments", departments.length);
	}

	private void initializeAdmin() {
		log.info("👤 Creating admin user...");

		Role adminRole = roleRepository.findByName("ADMIN")
				.orElseThrow();
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);

		User admin = User.builder()
				.username("admin")
				.email("admin@medicare.com")
				.password(passwordEncoder.encode("admin123"))
				.fullName("System Administrator")
				.phone("9876543210")
				.enabled(true)
				.roles(roles)
				.build();

		userRepository.save(admin);
		log.info("✓ Admin user created: username=admin, password=admin123");
	}

	private void initializeDoctors() {
		log.info("👨‍⚕️ Creating doctors...");

		Role doctorRole = roleRepository.findByName("DOCTOR")
				.orElseThrow();

		Object[][] doctorsData = {
				// {username, email, name, phone, specialization, license, experience,
				// qualifications, department}
				{ "dr.smith", "dr.smith@medicare.com", "Dr. John Smith", "9876543211",
						"Cardiologist", "LIC-CARD-001", 10,
						"MBBS, MD (Cardiology), DM (Cardiology)", "Cardiology" },

				{ "dr.jones", "dr.jones@medicare.com", "Dr. Sarah Jones", "9876543212",
						"Neurologist", "LIC-NEUR-002", 8, "MBBS, MD (Neurology), DM (Neurology)",
						"Neurology" },

				{ "dr.williams", "dr.williams@medicare.com", "Dr. Michael Williams", "9876543213",
						"Orthopedic Surgeon", "LIC-ORTH-003", 12, "MBBS, MS (Orthopedics), DNB",
						"Orthopedics" },

				{ "dr.brown", "dr.brown@medicare.com", "Dr. Emily Brown", "9876543214",
						"Pediatrician", "LIC-PEDI-004", 7,
						"MBBS, MD (Pediatrics), Fellowship in Neonatology", "Pediatrics" },

				{ "dr.davis", "dr.davis@medicare.com", "Dr. Robert Davis", "9876543215",
						"Dermatologist", "LIC-DERM-005", 6, "MBBS, MD (Dermatology), DDV",
						"Dermatology" },

				{ "dr.miller", "dr.miller@medicare.com", "Dr. Jessica Miller", "9876543216",
						"Ophthalmologist", "LIC-OPHT-006", 9, "MBBS, MS (Ophthalmology), FICO",
						"Ophthalmology" },

				{ "dr.wilson", "dr.wilson@medicare.com", "Dr. David Wilson", "9876543217",
						"ENT Specialist", "LIC-ENT-007", 11,
						"MBBS, MS (ENT), Fellowship in Rhinology", "ENT" },

				{ "dr.moore", "dr.moore@medicare.com", "Dr. Amanda Moore", "9876543218",
						"General Physician", "LIC-GP-008", 5, "MBBS, MD (General Medicine)",
						"General Medicine" },

				{ "dr.taylor", "dr.taylor@medicare.com", "Dr. Jennifer Taylor", "9876543219",
						"Gynecologist", "LIC-GYNE-009", 10,
						"MBBS, MD (Obstetrics & Gynecology), DGO", "Gynecology" },

				{ "dr.anderson", "dr.anderson@medicare.com", "Dr. Christopher Anderson",
						"9876543220", "Psychiatrist", "LIC-PSY-010", 8,
						"MBBS, MD (Psychiatry), MRCPsych", "Psychiatry" } };

		for (Object[] data : doctorsData) {
			Set<Role> roles = new HashSet<>();
			roles.add(doctorRole);

			User user = User.builder()
					.username((String) data[0])
					.email((String) data[1])
					.password(passwordEncoder.encode("admin123"))
					.fullName((String) data[2])
					.phone((String) data[3])
					.enabled(true)
					.roles(roles)
					.build();
			user = userRepository.save(user);

			Department department = departmentRepository.findByName((String) data[8])
					.orElseThrow(() -> new RuntimeException("Department not found: " + data[8]));

			Doctor doctor = Doctor.builder()
					.user(user)
					.specialization((String) data[4])
					.licenseNumber((String) data[5])
					.experience((Integer) data[6])
					.qualifications((String) data[7])
					.department(department)
					.build();
			doctorRepository.save(doctor);
		}

		log.info("✓ Created {} doctors", doctorsData.length);
	}

	private void initializePatients() {
		log.info("🧑‍🤝‍🧑 Creating patients...");

		Role patientRole = roleRepository.findByName("PATIENT")
				.orElseThrow();

		Object[][] patientsData = {
				// {username, email, name, phone, dob, gender, bloodGroup, address,
				// medicalHistory}
				{ "patient1", "alice.johnson@gmail.com", "Alice Johnson", "9876543221",
						"1990-05-15", "Female", "A+", "123 Main Street, New York, NY 10001",
						"No major illnesses. Allergic to penicillin." },

				{ "patient2", "bob.miller@gmail.com", "Bob Miller", "9876543222", "1985-08-20",
						"Male", "O+", "456 Oak Avenue, Los Angeles, CA 90001",
						"Hypertension diagnosed in 2020. Taking regular medication." },

				{ "patient3", "carol.white@gmail.com", "Carol White", "9876543223", "1992-03-10",
						"Female", "B+", "789 Pine Road, Chicago, IL 60601",
						"Type 2 Diabetes managed with diet and exercise." },

				{ "patient4", "david.clark@gmail.com", "David Clark", "9876543224", "1988-11-25",
						"Male", "AB+", "321 Elm Street, Houston, TX 77001",
						"Asthma since childhood. Uses inhaler as needed." },

				{ "patient5", "emma.garcia@gmail.com", "Emma Garcia", "9876543225", "1995-07-30",
						"Female", "O-", "654 Maple Drive, Phoenix, AZ 85001",
						"Healthy. No chronic conditions. Annual checkup regular." },

				{ "patient6", "frank.martinez@gmail.com", "Frank Martinez", "9876543226",
						"1983-12-05", "Male", "A-", "987 Cedar Lane, Philadelphia, PA 19101",
						"Previous knee surgery in 2018. Fully recovered." },

				{ "patient7", "grace.lee@gmail.com", "Grace Lee", "9876543227", "1991-09-18",
						"Female", "B-", "147 Birch Court, San Antonio, TX 78201",
						"Migraine headaches. Taking preventive medication." },

				{ "patient8", "henry.wilson@gmail.com", "Henry Wilson", "9876543228", "1987-04-22",
						"Male", "AB-", "258 Spruce Avenue, San Diego, CA 92101",
						"High cholesterol. Following low-fat diet plan." } };

		for (Object[] data : patientsData) {
			Set<Role> roles = new HashSet<>();
			roles.add(patientRole);

			User user = User.builder()
					.username((String) data[0])
					.email((String) data[1])
					.password(passwordEncoder.encode("admin123"))
					.fullName((String) data[2])
					.phone((String) data[3])
					.enabled(true)
					.roles(roles)
					.build();
			user = userRepository.save(user);

			Patient patient = Patient.builder()
					.user(user)
					.dateOfBirth(LocalDate.parse((String) data[4]))
					.gender((String) data[5])
					.bloodGroup((String) data[6])
					.address((String) data[7])
					.medicalHistory((String) data[8])
					.build();
			patientRepository.save(patient);
		}

		log.info("✓ Created {} patients", patientsData.length);
	}

	private void initializeAppointments() {
		log.info("📅 Creating sample appointments...");

		// Get some patients and doctors
		Patient patient1 = patientRepository.findByUserId(userRepository.findByUsername("patient1")
				.orElseThrow()
				.getId())
				.orElseThrow();

		Patient patient2 = patientRepository.findByUserId(userRepository.findByUsername("patient2")
				.orElseThrow()
				.getId())
				.orElseThrow();

		Patient patient3 = patientRepository.findByUserId(userRepository.findByUsername("patient3")
				.orElseThrow()
				.getId())
				.orElseThrow();

		Doctor drSmith = doctorRepository.findByUserId(userRepository.findByUsername("dr.smith")
				.orElseThrow()
				.getId())
				.orElseThrow();

		Doctor drJones = doctorRepository.findByUserId(userRepository.findByUsername("dr.jones")
				.orElseThrow()
				.getId())
				.orElseThrow();

		Doctor drWilliams = doctorRepository
				.findByUserId(userRepository.findByUsername("dr.williams")
						.orElseThrow()
						.getId())
				.orElseThrow();

		// Completed Appointments (Past dates)
		Appointment apt1 = Appointment.builder()
				.patient(patient1)
				.doctor(drSmith)
				.appointmentDate(LocalDate.now()
						.minusDays(10))
				.appointmentTime(LocalTime.of(10, 0))
				.symptoms("Chest pain and irregular heartbeat for the past 3 days")
				.diagnosis(
						"Mild arrhythmia detected. ECG shows irregular rhythm. Prescribed medication and lifestyle changes. Follow-up in 2 weeks.")
				.status("COMPLETED")
				.build();
		appointmentRepository.save(apt1);

		Appointment apt2 = Appointment.builder()
				.patient(patient2)
				.doctor(drJones)
				.appointmentDate(LocalDate.now()
						.minusDays(7))
				.appointmentTime(LocalTime.of(14, 30))
				.symptoms("Severe headache, dizziness, and blurred vision")
				.diagnosis(
						"Migraine with aura. Neurological examination normal. Prescribed preventive medication and pain relief.")
				.status("COMPLETED")
				.build();
		appointmentRepository.save(apt2);

		Appointment apt3 = Appointment.builder()
				.patient(patient3)
				.doctor(drWilliams)
				.appointmentDate(LocalDate.now()
						.minusDays(5))
				.appointmentTime(LocalTime.of(11, 0))
				.symptoms("Knee pain after morning jog, difficulty in walking")
				.diagnosis(
						"Mild ligament strain. Recommended rest, ice therapy, and physiotherapy. Prescribed anti-inflammatory medication.")
				.status("COMPLETED")
				.build();
		appointmentRepository.save(apt3);

		// Scheduled Appointments (Future dates)
		Appointment apt4 = Appointment.builder()
				.patient(patient1)
				.doctor(drSmith)
				.appointmentDate(LocalDate.now()
						.plusDays(2))
				.appointmentTime(LocalTime.of(9, 30))
				.symptoms(
						"Follow-up appointment for heart condition. Checking medication effectiveness.")
				.status("SCHEDULED")
				.build();
		appointmentRepository.save(apt4);

		Appointment apt5 = Appointment.builder()
				.patient(patient2)
				.doctor(drJones)
				.appointmentDate(LocalDate.now()
						.plusDays(5))
				.appointmentTime(LocalTime.of(15, 0))
				.symptoms("Routine checkup for blood pressure monitoring")
				.status("SCHEDULED")
				.build();
		appointmentRepository.save(apt5);

		Appointment apt6 = Appointment.builder()
				.patient(patient3)
				.doctor(drWilliams)
				.appointmentDate(LocalDate.now()
						.plusDays(3))
				.appointmentTime(LocalTime.of(10, 30))
				.symptoms("Knee pain persists, need to review treatment plan")
				.status("SCHEDULED")
				.build();
		appointmentRepository.save(apt6);

		log.info("✓ Created 6 sample appointments (3 completed, 3 scheduled)");
	}

	private void initializePrescriptions() {
		log.info("💊 Creating sample prescriptions...");

		// Get completed appointments
		Appointment apt1 = appointmentRepository.findById(1L)
				.orElse(null);
		Appointment apt2 = appointmentRepository.findById(2L)
				.orElse(null);
		Appointment apt3 = appointmentRepository.findById(3L)
				.orElse(null);

		if (apt1 != null) {
			// Prescriptions for first appointment (Heart condition)
			Prescription rx1 = Prescription.builder()
					.appointment(apt1)
					.medicineName("Metoprolol 50mg")
					.dosage("1 tablet twice daily")
					.duration(30)
					.instructions("Take after breakfast and dinner. Do not skip doses.")
					.build();
			prescriptionRepository.save(rx1);

			Prescription rx2 = Prescription.builder()
					.appointment(apt1)
					.medicineName("Aspirin 75mg")
					.dosage("1 tablet once daily")
					.duration(30)
					.instructions("Take after dinner. Blood thinner - avoid injury.")
					.build();
			prescriptionRepository.save(rx2);
		}

		if (apt2 != null) {
			// Prescriptions for second appointment (Migraine)
			Prescription rx3 = Prescription.builder()
					.appointment(apt2)
					.medicineName("Sumatriptan 50mg")
					.dosage("1 tablet when headache starts")
					.duration(15)
					.instructions("Take at onset of migraine. Maximum 2 tablets in 24 hours.")
					.build();
			prescriptionRepository.save(rx3);

			Prescription rx4 = Prescription.builder()
					.appointment(apt2)
					.medicineName("Propranolol 40mg")
					.dosage("1 tablet twice daily")
					.duration(30)
					.instructions("Preventive medication. Take regularly even without headache.")
					.build();
			prescriptionRepository.save(rx4);
		}

		if (apt3 != null) {
			// Prescriptions for third appointment (Knee pain)
			Prescription rx5 = Prescription.builder()
					.appointment(apt3)
					.medicineName("Ibuprofen 400mg")
					.dosage("1 tablet three times daily")
					.duration(7)
					.instructions("Take after meals. Anti-inflammatory for pain relief.")
					.build();
			prescriptionRepository.save(rx5);

			Prescription rx6 = Prescription.builder()
					.appointment(apt3)
					.medicineName("Diclofenac Gel")
					.dosage("Apply on affected area twice daily")
					.duration(14)
					.instructions("Apply thin layer and massage gently. For external use only.")
					.build();
			prescriptionRepository.save(rx6);
		}

		log.info("✓ Created 6 sample prescriptions");
	}

	private void printCredentials() {
		log.info("\n" + "=".repeat(80));
		log.info("📋 SAMPLE CREDENTIALS FOR TESTING");
		log.info("=".repeat(80));

		log.info("\n👤 ADMIN ACCOUNT:");
		log.info("   Username: admin");
		log.info("   Password: admin123");
		log.info("   Email: admin@medicare.com");

		log.info("\n👨‍⚕️ DOCTOR ACCOUNTS (All passwords: admin123):");
		log.info("   1. dr.smith     - Cardiologist");
		log.info("   2. dr.jones     - Neurologist");
		log.info("   3. dr.williams  - Orthopedic Surgeon");
		log.info("   4. dr.brown     - Pediatrician");
		log.info("   5. dr.davis     - Dermatologist");
		log.info("   (and 5 more doctors...)");

		log.info("\n🧑‍🤝‍🧑 PATIENT ACCOUNTS (All passwords: admin123):");
		log.info("   1. patient1 - Alice Johnson");
		log.info("   2. patient2 - Bob Miller");
		log.info("   3. patient3 - Carol White");
		log.info("   (and 5 more patients...)");

		log.info("\n📊 SAMPLE DATA:");
		log.info("   ✓ 10 Departments");
		log.info("   ✓ 10 Doctors");
		log.info("   ✓ 8 Patients");
		log.info("   ✓ 6 Appointments (3 completed, 3 scheduled)");
		log.info("   ✓ 6 Prescriptions");

		log.info("\n🚀 APPLICATION READY!");
		log.info("   Visit: http://localhost:8080");
		log.info("=".repeat(80) + "\n");
	}
}