package com.healthtrack.backend.Cofiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.healthtrack.backend.Service.CustomUserDetailService;
import com.healthtrack.backend.Utility.JwtFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

	private final JwtFilter jwtFilter;
	private final CustomUserDetailService customUserDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.cors().and().csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

								.requestMatchers(HttpMethod.POST, "/api/billing/appointment/*").hasRole("ADMIN")
								.requestMatchers(HttpMethod.GET, "/api/billing/*").hasRole("ADMIN")

					            .requestMatchers(HttpMethod.GET, "/api/doctor/all").hasAnyRole("DOCTOR","PATIENT") 
								.requestMatchers("/api/doctor/**").hasRole("DOCTOR")
								.requestMatchers(HttpMethod.GET, "/api/patient/doctor/*").hasRole("DOCTOR")

								.requestMatchers("/api/patient/**").hasRole("PATIENT")

								.anyRequest().authenticated())
				.userDetailsService(customUserDetailService)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

}
