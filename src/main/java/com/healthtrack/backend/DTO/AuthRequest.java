package com.healthtrack.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
		@NotBlank(message = "Email is required") @Email(message = "Please enter a valid email") String email,
		@NotBlank(message = "Password is required") String password,
	   String role) {

	public String email() {
		return email;
	}

	public String password() {
		return password;
	}

	public String role() {
		return role;
	}
	
	

}
