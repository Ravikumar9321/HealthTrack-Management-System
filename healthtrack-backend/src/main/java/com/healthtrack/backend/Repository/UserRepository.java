package com.healthtrack.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthtrack.backend.Entity.UserInfo;

public interface UserRepository  extends JpaRepository<UserInfo, Integer>{
	        Optional<UserInfo> findByEmail(String email);

}
