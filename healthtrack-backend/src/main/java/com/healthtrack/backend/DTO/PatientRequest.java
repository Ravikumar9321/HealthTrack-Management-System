package com.healthtrack.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PatientRequest(
		@NotBlank(message = "Name is required")
	    String name,
	    @NotNull(message = "Please enter email")
		@Email(message = "Email must be valid")
	    String email,
	    String gender,
	    @NotNull(message = "Please enter contact number")
		@Pattern(regexp = "\\d{10}", message = "Contact must be 10 digits")
	    String contact,
	    String medicalHistory,
	    String password
	) {
	
}
