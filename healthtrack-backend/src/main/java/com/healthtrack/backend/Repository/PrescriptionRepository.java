package com.healthtrack.backend.Repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.healthtrack.backend.Entity.Prescription;

public interface PrescriptionRepository  extends JpaRepository<Prescription, Integer>{



	@Query("select a.prescription from Appointment a where a.id=?1")
	Optional<Prescription> findPrescriptionByAppointmentId(Integer appointmentId);

}
