package com.emiperez.repeson.mapper;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class JacksonDataObject implements JsonDataObject {

	private static final ObjectReader READER = new ObjectMapper().reader(); 
	private JsonNode object;
	
	private JacksonDataObject() {}
	
	public JacksonDataObject(InputStream input) throws IOException {
		this();
		this.object = READER.readTree(input);
	}
	
	@Override
	public <T> T getAttribute(String attrName, Class<T> type) {
		try {
			return READER.readValue(object.get(attrName), type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAttributeAsText(String attrName) {
		return object.get(attrName).asText();
	}

}
