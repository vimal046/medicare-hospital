package com.hospital.medicare.exception;

@SuppressWarnings("serial")
public class DuplicateResourceException extends RuntimeException {

	public DuplicateResourceException(String message) {
		super(message);
	}

}
