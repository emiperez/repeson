package com.emiperez.commons.json.apiselector;

import java.io.IOException;
import java.io.InputStream;

import com.emiperez.repeson.client.JsonRpcException;

public interface JsonMapper {	

	String serialize(Object object) throws JsonRpcException;

	String serialize(Object object, boolean isNamedParams) throws JsonRpcException;
	
	<T> T deserialize(InputStream input, Class<T> type) throws IOException;
}
