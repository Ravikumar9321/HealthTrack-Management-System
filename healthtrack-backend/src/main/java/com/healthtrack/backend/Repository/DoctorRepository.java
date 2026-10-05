package com.healthtrack.backend.Repository;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.healthtrack.backend.Entity.Doctor;

public interface DoctorRepository  extends JpaRepository<Doctor, Integer>{

	Optional<Doctor> findDoctorByEmail(String email);


}
