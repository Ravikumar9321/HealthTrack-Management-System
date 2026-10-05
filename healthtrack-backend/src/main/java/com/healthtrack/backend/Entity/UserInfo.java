package com.healthtrack.backend.Entity;




import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private String role; // e.g., ROLE_ADMIN, ROLE_DOCTOR
    
    @OneToOne(mappedBy = "userinfo", cascade = CascadeType.ALL)
    private Doctor doctor; 
}
