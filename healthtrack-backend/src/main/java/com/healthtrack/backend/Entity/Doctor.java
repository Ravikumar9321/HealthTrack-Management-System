package com.healthtrack.backend.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Doctor name is required")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;
    
	@Email(message = "Email must be valid")
	private String email;

	@NotBlank(message = "Schedule is required")
	private String schedule; // e.g. "Mon-Fri 09:00-17:00"

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;
   
    
    @OneToOne
    @JoinColumn(name = "userinfo_id")
    @JsonIgnore
    private UserInfo userinfo;  
}
