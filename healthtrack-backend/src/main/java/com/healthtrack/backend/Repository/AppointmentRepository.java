package com.healthtrack.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.healthtrack.backend.Entity.Appointment;
import com.healthtrack.backend.Entity.Patient;

public interface AppointmentRepository  extends JpaRepository<Appointment, Integer>{
	
	@Query("select d.appointments from Doctor d where d.id=?1")
	List<Appointment> getAppointmentsByDoctorId(Integer doctorId);

	
	@Query("select a.patient from Appointment a where a.id=?1")
	Optional<Patient> getPatientByAppointmentId(Integer appointmentId);

	
     @Query("select p.appointments from Patient p where p.id=?1")
	List<Appointment> getAppointmentsByPatientId(Integer patientId);

}
