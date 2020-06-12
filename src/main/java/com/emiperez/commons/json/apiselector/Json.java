package com.emiperez.commons.json.apiselector;

import java.util.logging.Logger;
/**
 * This Singleton loads the api selected in the dependences of the project, 
 * and hides it behind the JsonMapper interface.
 */
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
