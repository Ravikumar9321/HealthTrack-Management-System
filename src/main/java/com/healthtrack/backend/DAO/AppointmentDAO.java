package com.healthtrack.backend.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.healthtrack.backend.Entity.Appointment;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Repository.AppointmentRepository;

@Repository
public class AppointmentDAO {
	@Autowired
	private AppointmentRepository repository;

	public Appointment bookAppointment(Appointment appointment) {
        		return repository.save(appointment);
	}

	public Optional<Appointment> findAppointmentById(Integer appointmentId) {
           return repository.findById(appointmentId);	
	}

	public List<Appointment> findAllAppointment() {
         return repository.findAll();		
	}

	public Appointment updateAppointment(Appointment appointment) {
     return repository.save(appointment);		
	}

	public void deleteAppointmentById(Integer appointmentId) {
        repository.deleteById(appointmentId);		
	}

	
	public List<Appointment> getAppointmentsByDoctorId(Integer doctorId) {
		return repository.getAppointmentsByDoctorId(doctorId);
	}

	public Optional<Patient> getPatientByAppointmentId(Integer appointmentId) {
		return repository.getPatientByAppointmentId(appointmentId);
		
	}

	public List<Appointment> getAppointmentsByPatientId(Integer patientId) {
		 return repository.getAppointmentsByPatientId(patientId);
		
	}

}
