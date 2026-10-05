package com.healthtrack.backend.DTO;

public record AuthResponse(String message,String token) {

	public String message() {
		return message;
	}

	public String token() {
		return token;
	}

	
}
