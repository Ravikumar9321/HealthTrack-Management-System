package com.healthtrack.backend.Entity;

import jakarta.persistence.*;



import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDate date;
	
	private LocalTime time;

	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patient;

	@ManyToOne
	@JoinColumn(name = "doctor_id", nullable = false)
	private Doctor doctor;

	@OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
	@JsonIgnore
	private Prescription prescription;

	@OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
	@JsonIgnore
	private Billing billing;

}
