package com.healthtrack.backend.Controller;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthtrack.backend.DTO.AuthRequest;
import com.healthtrack.backend.DTO.AuthResponse;
import com.healthtrack.backend.DTO.DoctorRequest;
import com.healthtrack.backend.DTO.LoginResponse;
import com.healthtrack.backend.DTO.PatientRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Doctor;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Entity.UserInfo;
import com.healthtrack.backend.Exception.ResourceNotFoundException;
import com.healthtrack.backend.Repository.DoctorRepository;
import com.healthtrack.backend.Repository.PatientRepository;
import com.healthtrack.backend.Repository.UserRepository;
import com.healthtrack.backend.Service.DoctorService;
import com.healthtrack.backend.Service.PatientService;
import com.healthtrack.backend.Utility.JwtUtil;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:3000", "https://health-track-management-system.vercel.app" })
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
     private final DoctorService doctorService;
     private final PatientService patientService;
    // -------------------- GENERIC REGISTER --------------------
     @PostMapping("/register")
     public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest request) {

         if (repository.findByEmail(request.email()).isPresent()) {
             return ResponseEntity.status(HttpStatus.CONFLICT)
                     .body(new AuthResponse("User already exists", null));
         }

         String role = request.role() == null ? "ROLE_USER" : request.role();

         // Validate doctor/patient before saving UserInfo
         if ("ROLE_DOCTOR".equals(role)) {
             Doctor doctor = doctorRepository.findDoctorByEmail(request.email())
                     .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with email " + request.email()));

             UserInfo userInfo = UserInfo.builder()
                     .email(request.email())
                     .password(passwordEncoder.encode(request.password()))
                     .role(role)
                     .build();

             UserInfo savedUserInfo = repository.save(userInfo);
             doctor.setUserinfo(savedUserInfo);
             doctorRepository.save(doctor);

         } else if ("ROLE_PATIENT".equals(role)) {
             Patient patient = patientRepository.findByEmail(request.email())
                     .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email " + request.email()));

             UserInfo userInfo = UserInfo.builder()
                     .email(request.email())
                     .password(passwordEncoder.encode(request.password()))
                     .role(role)
                     .build();

             repository.save(userInfo);
             patientRepository.save(patient);

         } else {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                     .body(new AuthResponse("Invalid role provided", null));
         }

         return ResponseEntity.status(HttpStatus.CREATED)
                 .body(new AuthResponse("User registered successfully", null));
     }

    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody AuthRequest request) {
        UserInfo userInfo = repository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + request.email()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtil.generateToken(userInfo.getEmail(), userInfo.getRole());

        String defaultLandingPage = switch (userInfo.getRole()) {
            case "ROLE_ADMIN" -> "/admin";
            case "ROLE_DOCTOR" -> "/profile";
            case "ROLE_PATIENT" -> "/patientDashboard";
            default -> "/";
        };

        Integer doctorId = null;
        Integer patientId = null;

        if ("ROLE_DOCTOR".equals(userInfo.getRole())) {
            Doctor doctor = doctorRepository.findDoctorByEmail(userInfo.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with email " + userInfo.getEmail()));
            doctorId = doctor.getId();
        }

        if ("ROLE_PATIENT".equals(userInfo.getRole())) {
            Patient patient = patientRepository.findByEmail(userInfo.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email " + userInfo.getEmail()));
            patientId = patient.getId();
        }

        return ResponseEntity.ok(new LoginResponse(token, userInfo.getRole(), defaultLandingPage, doctorId, patientId));
    }


	@PostMapping("/doctor-register")
	public ResponseEntity<ResponseStructure<Doctor>> registerDoctor(@Valid @RequestBody DoctorRequest request){
		return doctorService.registerDoctor(request);
	} 
	
	@Hidden
	@PostMapping("/admin-register")
	public ResponseEntity<UserInfo> adminRegister(@Valid @RequestBody UserInfo userInfo){
		  userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		   
		 return  ResponseEntity.status(HttpStatus.CREATED).body(repository.save(userInfo));
	}

	
	@PostMapping("/patient-register")
	public ResponseEntity<ResponseStructure<Patient>> registerPatient(@Valid @RequestBody PatientRequest request){
		return patientService.registerPatient(request); 
	}
 
}
