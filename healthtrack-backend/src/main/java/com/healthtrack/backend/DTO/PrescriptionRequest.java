package com.healthtrack.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class PrescriptionRequest {
    @NotBlank(message = "Medicine name is required")
    private String medicines;

    @NotBlank(message = "Dosage is required")
    private String dosage;
    @NotBlank(message = "notes is required")
    private String notes;

}
