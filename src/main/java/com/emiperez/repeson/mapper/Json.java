package com.emiperez.repeson.mapper;

import java.util.logging.Logger;

public enum Json {
	INSTANCE;

	private JsonMapper api;

	Json() {
		Logger log = Logger.getLogger("Json.INSTANCE");
		try {
			Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
			api = new JacksonJsonMapper();
		} catch (ClassNotFoundException e) {
			log.info("com.fasterxml.jackson.databind.ObjectMapper Not Found.");
		}
		if (api == null) {
			try {
				Class.forName("jakarta.json.bind.Jsonb");
				api = new YassonJsonMapper();
			} catch (ClassNotFoundException e) {
				throw new ExceptionInInitializerError(new NoJsonApiException("No Json Api Found. Check your project's dependencies."));
			}
		}
	}
	
	public JsonMapper api() {
		return api;
	}
}
