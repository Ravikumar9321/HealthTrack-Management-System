package com.healthtrack.backend.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentUpdateRequest {
	 @NotNull(message = "Date is required")
	    private LocalDate date;

	    @NotNull(message = "Time is required")
	    private LocalTime time;

	    @NotNull(message = "Patient ID is required")
	    private Integer patientId;

	    @NotNull(message = "Doctor ID is required")
	    private Integer doctorId;
}
