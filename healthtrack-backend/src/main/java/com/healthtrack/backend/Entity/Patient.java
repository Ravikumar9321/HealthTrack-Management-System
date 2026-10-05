package com.healthtrack.backend.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Please enter email")
	@Email(message = "Email must be valid")
	private String email;
	
	private String gender;

	@NotNull(message = "Please enter contact number")
	@Pattern(regexp = "\\d{10}", message = "Contact must be 10 digits")
	private String contact;
	

	private String medicalHistory;
	
	@OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Appointment> appointments;
	
 
}
