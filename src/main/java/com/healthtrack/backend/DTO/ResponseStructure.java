package com.healthtrack.backend.DTO;

import lombok.*;

@Data

public class ResponseStructure<T> {
	private Integer statusCode;
	private String message;
	private T data;

	
}
