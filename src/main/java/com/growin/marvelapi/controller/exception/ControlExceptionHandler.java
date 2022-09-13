package com.growin.marvelapi.controller.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.growin.marvelapi.exception.GenericError;
import com.growin.marvelapi.exception.InvalidParameterException;
import com.growin.marvelapi.exception.MarvelApiException;
import com.growin.marvelapi.exception.ObjectNotFoundException;
import com.growin.marvelapi.exception.ServiceUnavailable;

@ControllerAdvice
public class ControlExceptionHandler {

	/**
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<GenericError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		var err = new GenericError(LocalDateTime.now(), status.value(), "Not found", request.getRequestURI(), null, e.getMessage());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<GenericError> invalidParameter(InvalidParameterException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		var err = new GenericError(LocalDateTime.now(), status.value(), "Invalid request", request.getRequestURI(), null, e.getMessage());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MarvelApiException.class)
	public ResponseEntity<GenericError> marvelApi(MarvelApiException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		var err = new GenericError(LocalDateTime.now(), status.value(), "Internal server error", request.getRequestURI(), null, e.getMessage());
		return ResponseEntity.status(status).body(err);
	}

	/**
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ServiceUnavailable.class)
	public ResponseEntity<GenericError> servicUnavailable(ServiceUnavailable e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
		var err = new GenericError(LocalDateTime.now(), status.value(), "Unavailable service", request.getRequestURI(), null, e.getMessage());
		return ResponseEntity.status(status).body(err);
	}

}
