package com.healthtrack.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.healthtrack.backend.Entity.Billing;

public interface BillingRepository  extends JpaRepository<Billing, Integer>{

	@Query("select b from Billing b where b.appointment.id=?1")
	Optional<Billing> findBillingDetailstByAppointmentId(Integer appointmentId);

}
