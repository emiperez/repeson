package com.emiperez.repeson.mapper;

import java.io.IOException;
import java.io.InputStream;

import com.emiperez.repeson.client.JsonRpcException;

public interface JsonMapper {
	
	JsonDataObject getDataObject(InputStream input) throws IOException;

	String serialize(Object object) throws JsonRpcException;

	String serialize(Object object, boolean isNamedParams) throws JsonRpcException;
	
	<T> T deserialize(InputStream input, Class<T> type) throws IOException;
}
