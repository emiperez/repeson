package com.emiperez.commons.json.apiselector;

import java.io.IOException;
import java.io.InputStream;

import com.emiperez.repeson.client.JsonRpcException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonMapper implements JsonMapper {

	private static final ObjectMapper MAPPER = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);	
	
	@Override
	public String serialize(Object object) throws JsonRpcException {
		try {
			return MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonRpcException("Json Serialization Error for " + object.getClass().getName(), e);
		}
	}	

	@Override
	public String serializeAsArray(Object params) throws JsonRpcException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configOverride(params.getClass()).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY));
		try {
			return mapper.writeValueAsString(params);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonRpcException("Json Serialization Error for " + params.getClass().getName(), e);
		}
	}

	@Override
	public <T> T deserialize(InputStream input, Class<T> type) throws IOException {
		return MAPPER.readValue(input, type);
	}
	

}
