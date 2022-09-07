package com.growin.marvelapi.exception;

/**
 * @author Guilherme Vital
 *
 */
public class MarvelApiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MarvelApiException(String msg) {
		super(msg);
	}
}
