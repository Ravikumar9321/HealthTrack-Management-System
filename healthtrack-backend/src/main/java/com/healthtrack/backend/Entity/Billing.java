package com.healthtrack.backend.Entity;

import jakarta.persistence.*;

import lombok.Data;
import java.math.BigDecimal;

import com.healthtrack.backend.DTO.BillingStatus;

@Entity
@Data
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

 
    private BigDecimal amount;  

    @Enumerated(EnumType.STRING)
    private BillingStatus status;
    
    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)    
    private Appointment appointment;
}
