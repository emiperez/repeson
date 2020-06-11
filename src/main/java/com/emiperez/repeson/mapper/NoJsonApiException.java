package com.emiperez.repeson.mapper;

public class NoJsonApiException extends Exception{
	
	public NoJsonApiException(String message) {
		super(message);
	}

	public NoJsonApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
