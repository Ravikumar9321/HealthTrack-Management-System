package com.healthtrack.backend.Service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthtrack.backend.DAO.DoctorDAO;
import com.healthtrack.backend.DTO.AuthResponse;
import com.healthtrack.backend.DTO.DoctorRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Doctor;
import com.healthtrack.backend.Entity.UserInfo;
import com.healthtrack.backend.Exception.ResourceNotFoundException;
import com.healthtrack.backend.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

	private final DoctorDAO doctordao;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, String message, T data) {
		ResponseStructure<T> response = new ResponseStructure<>();
		response.setStatusCode(status.value());
		response.setMessage(message);
		response.setData(data);
		return ResponseEntity.status(status).body(response);
	}

	@Transactional
public ResponseEntity<ResponseStructure<Doctor>> registerDoctor(DoctorRequest request) {
    if (request.email() == null || request.email().isBlank()) {
        throw new IllegalArgumentException("Email is required for doctor registration");
    }

    
    if (userRepository.findByEmail(request.email()).isPresent()) {
        throw new DataIntegrityViolationException("Email already exists");
    }

    
    UserInfo userInfo = new UserInfo();
    userInfo.setEmail(request.email());
    userInfo.setPassword(passwordEncoder.encode(request.password()));
    userInfo.setRole("ROLE_DOCTOR");
    UserInfo savedUserInfo = userRepository.save(userInfo);

    
    Doctor doctor = new Doctor();
    doctor.setName(request.name());
    doctor.setSpecialization(request.specialization());
    doctor.setEmail(request.email());
    doctor.setSchedule(request.schedule());
    doctor.setUserinfo(savedUserInfo); 

    Doctor savedDoctor = doctordao.registerDoctor(doctor);

    return buildResponse(HttpStatus.CREATED,
            "Doctor registered successfully: " + savedDoctor.getName(),
            savedDoctor);
}


	public ResponseEntity<ResponseStructure<Doctor>> findDoctorById(Integer doctorId) {
		Doctor doctor = doctordao.findDoctorById(doctorId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID " + doctorId));
		return buildResponse(HttpStatus.OK, "Doctor details found", doctor);
	}

	public ResponseEntity<ResponseStructure<List<Doctor>>> findAllDoctors() {
		List<Doctor> doctors = doctordao.findAllDoctor();
		if (doctors.isEmpty()) {
			throw new ResourceNotFoundException("No Doctor records found");
		}
		return buildResponse(HttpStatus.OK, "Doctor details found", doctors);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Doctor>> updateDoctor(Doctor doctor, Integer doctorId) {
		Doctor existedDoctor = doctordao.findDoctorById(doctorId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID " + doctorId));
		existedDoctor.setName(doctor.getName());
		existedDoctor.setSchedule(doctor.getSchedule());
		existedDoctor.setEmail(doctor.getEmail());
		existedDoctor.setSpecialization(doctor.getSpecialization());

		Doctor updatedDoctor = doctordao.updateDoctor(existedDoctor);
		return buildResponse(HttpStatus.OK, "Doctor updated successfully", updatedDoctor);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<String>> deleteDoctorById(Integer doctorId) {
		doctordao.findDoctorById(doctorId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID " + doctorId));
		doctordao.deleteDoctorById(doctorId);
		return buildResponse(HttpStatus.OK, "Doctor deleted successfully with ID " + doctorId, null);
	}

	public ResponseEntity<ResponseStructure<Doctor>> findDoctorByEmail(String email) {
		Doctor doctor = doctordao.findDoctorByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Details not found with " + email));
		return buildResponse(HttpStatus.OK, "Details found", doctor);
	}

}
