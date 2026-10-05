package com.healthtrack.backend.Utility;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {
	
	private final String SECRET="mySuperSecretKeyForJwtToken1234567890";
	private  final long EXPIRATION=1000*60*15;
	private final Key secretKey=Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	
	public String generateToken(String email,String role) {
		return Jwts.builder()
				.setSubject(email)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
				.signWith(secretKey,SignatureAlgorithm.HS256)
				.compact();
	}
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String extractEmail(String token) {
		return getClaims(token).getSubject();
	}
	
	public String extractRole(String token) {
		return getClaims(token).get("role",String.class);
	}
	
	public boolean isValidToken(String token) {
		try {
			return !getClaims(token).getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

}
