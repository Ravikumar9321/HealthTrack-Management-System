package com.healthtrack.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorRequest(
	    @NotBlank(message = "Doctor name is required")
	    String name,
	    @NotBlank(message = "Specialization is required")
	    String specialization,
		@Email(message = "Email must be valid")
	    String email,
		@NotBlank(message = "Schedule is required")
	    String schedule,
	    String password
	) {
	
}
