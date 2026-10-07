package com.healthtrack.backend.DTO;

public record LoginResponse(
    String token,
    String role,
    String defaultLandingPage,
    Integer doctorId,
    Integer patientId
) {
	
}