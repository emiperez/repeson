package com.emiperez.repeson.mapper;

import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class YassonDataObject implements JsonDataObject {
	
	private static final Jsonb jsonb = JsonbBuilder.create();
	
	private JsonObject object;
	
	private YassonDataObject() {}
	
	public YassonDataObject(InputStream input) {
		this();		
		object = Json.createReader(input).readObject();
	}

	@Override
	public <T> T getAttribute(String attrName, Class<T> type) throws IOException {
		T result = jsonb.fromJson(getAttributeAsText(attrName), type);
		return result;
	}

	@Override
	public String getAttributeAsText(String attrName) throws IOException {
		JsonValue obj = object.get(attrName);
		return obj.toString();
	}

}
