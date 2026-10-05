package com.healthtrack.backend.Exception;

import java.util.HashMap;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.healthtrack.backend.DTO.ResponseStructure;

@ControllerAdvice
public class GlobalExcecptionHandler extends ResponseEntityExceptionHandler {
	
	private <T> ResponseEntity<ResponseStructure<T>> handleErrorResponse(HttpStatus status,String message,T data ){
		    ResponseStructure<T> response=new ResponseStructure<T>();
		    response.setStatusCode(status.value());
		    response.setMessage(message);
		    response.setData(data);
		    return ResponseEntity.status(status).body(response);
		    }

	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode staus, WebRequest request) {
		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

		ResponseStructure<Map<String, String>> response = new ResponseStructure<Map<String, String>>();
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage("Validation failed. Please correct the highlighted fields.");
		response.setData(fieldErrors);
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseStructure<String>> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
	    return handleErrorResponse(HttpStatus.CONFLICT, 
	            "Duplicate entry or constraint violation: " + exception.getMessage(), null);
	}

	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>>  handleResourceNotFound(ResourceNotFoundException exception){
		  return handleErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
	}
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>>  handleUserNotFound(UsernameNotFoundException
			exception){
		  return handleErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
	}

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseStructure<String>> handleGeneralException(Exception exception){
		return handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), null);
	}
 
}
