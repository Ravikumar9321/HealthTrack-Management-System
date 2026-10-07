package com.healthtrack.backend.Service;

import java.util.List;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthtrack.backend.DAO.PatientDAO;
import com.healthtrack.backend.DTO.PatientRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Entity.UserInfo;
import com.healthtrack.backend.Exception.ResourceNotFoundException;
import com.healthtrack.backend.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientDAO patientdao;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status,
                                                                   String message,
                                                                   T data) {
        ResponseStructure<T> response = new ResponseStructure<>();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.status(status).body(response);
    }
    @Transactional
public ResponseEntity<ResponseStructure<Patient>> registerPatient(PatientRequest request) {
    if (request.email() == null || request.email().isBlank()) {
        throw new IllegalArgumentException("Email is required for patient registration");
    }

    if (repository.findByEmail(request.email()).isPresent()) {
        throw new DataIntegrityViolationException("Email already exists");
    }

    UserInfo userInfo = new UserInfo();
    userInfo.setEmail(request.email());
    userInfo.setPassword(passwordEncoder.encode(request.password()));
    userInfo.setRole("ROLE_PATIENT");
    repository.save(userInfo);

    Patient patient = new Patient();
    patient.setName(request.name());
    patient.setEmail(request.email());   
    patient.setGender(request.gender());
    patient.setContact(request.contact());
    patient.setMedicalHistory(request.medicalHistory());

    Patient savedPatient = patientdao.registerPatient(patient);

    // Return patient details only (no token, no mapping)
    return buildResponse(HttpStatus.CREATED,
            "Patient registered successfully: " + savedPatient.getName(),
            savedPatient);
}

    public ResponseEntity<ResponseStructure<Patient>> findPatientById(Integer patientId) {
        Patient patient = patientdao.findPatientById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID " + patientId));
        return buildResponse(HttpStatus.OK, "Patient details found", patient);
    }

    public ResponseEntity<ResponseStructure<List<Patient>>> findAllPatients() {
        List<Patient> patients = patientdao.findAllPatient();
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("No patient records found");
        }
        return buildResponse(HttpStatus.OK, "Patient details found", patients);
    }

    @Transactional
    public ResponseEntity<ResponseStructure<Patient>> updatePatient(Patient patient, Integer patientId) {
        patientdao.findPatientById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID " + patientId));
             patient.setId(patientId);
        Patient updatedPatient = patientdao.updatePatient(patient);
        return buildResponse(HttpStatus.OK, "Patient updated successfully", updatedPatient);
    }
    
    @Transactional
    public ResponseEntity<ResponseStructure<String>> deletePatientById(Integer patientId) {
        patientdao.findPatientById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID " + patientId));
        patientdao.deletePatientById(patientId);
        return buildResponse(HttpStatus.OK, "Patient deleted successfully with ID " + patientId, null);
    }
	public ResponseEntity<ResponseStructure<List<Patient>>> findPatientsByDoctorId(Integer doctorId) {
		 List<Patient> patients = patientdao.findPatientsByDoctorId(doctorId);
	        if (patients.isEmpty()) {
	            throw new ResourceNotFoundException("No patient records found");
	        }
	        return buildResponse(HttpStatus.OK, "Patient details found", patients);
	}
	public ResponseEntity<ResponseStructure<Patient>> findPatientByEmail(String email) {
		  Patient patient = patientdao.findPatientByEmail(email)
	                .orElseThrow(() -> new ResourceNotFoundException("Details not found with " + email));
	        return buildResponse(HttpStatus.OK, "Details found", patient);

	}
}
