package com.healthtrack.backend.Service;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.healthtrack.backend.Entity.UserInfo;
import com.healthtrack.backend.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	             UserInfo userInfo = repository.findByEmail(email)
	            		 .orElseThrow(()->new UsernameNotFoundException("User not registered"));
	             
		return User.withUsername(userInfo.getEmail())
				.password(userInfo.getPassword())
				.authorities(userInfo.getRole())
				.build();
	}

}
