package com.emiperez.commons.json.apiselector;

public class NoJsonApiException extends Exception{
	
	public NoJsonApiException(String message) {
		super(message);
	}

	public NoJsonApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
