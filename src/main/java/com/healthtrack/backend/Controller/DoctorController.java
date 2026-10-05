package com.healthtrack.backend.Controller;

import java.util.List;





import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Doctor;
import com.healthtrack.backend.Service.DoctorService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctor")
@Tag(name = "Doctor",description = "Doctor related API's")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DoctorController {
	private final DoctorService service;
	
	
	
		
	@GetMapping("/{doctorId}")
	public ResponseEntity<ResponseStructure<Doctor>> findDoctorById( @PathVariable  Integer doctorId){
		return service.findDoctorById(doctorId);
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Doctor>>> findAllDoctors(){
		return service.findAllDoctors();
	}
	@PutMapping("/{doctorId}")
	public ResponseEntity<ResponseStructure<Doctor>> updateDoctor(@Valid @RequestBody Doctor doctor,@PathVariable Integer doctorId){
		return service.updateDoctor(doctor,doctorId);
	}
	
	@DeleteMapping("/{doctorId}")
	public ResponseEntity<ResponseStructure<String>> deleteDoctorById(  @PathVariable Integer doctorId){
		return service.deleteDoctorById(doctorId);
	}
	@GetMapping("/email/{email}")
	public ResponseEntity<ResponseStructure<Doctor>>  findDoctorByEmail ( @PathVariable String  email){
		return service.findDoctorByEmail(email);
	}
	
	
	

}
