package com.healthtrack.backend.DTO;

import java.math.BigDecimal;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BillingRequest {
	
	 @NotNull(message = "Amount is required")
	    @Positive(message = "Amount must be positive")
	    private BigDecimal amount;  

	 @NotNull(message = "Status is required")
	 @Enumerated(EnumType.STRING)
	    private BillingStatus status;

}
