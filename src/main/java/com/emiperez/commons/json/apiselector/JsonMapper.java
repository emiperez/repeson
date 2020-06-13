package com.emiperez.commons.json.apiselector;

import java.io.IOException;
import java.io.InputStream;

import com.emiperez.repeson.client.JsonRpcException;

/**
 * An interface to hide the Json Api selected in dependencies.
 */
public interface JsonMapper {

	/**
	 * @param pojo The object to be serialized
	 * @return The JSON representation of the object.
	 * @throws JsonRpcException if an error occurs in serialization
	 */
	String serialize(Object pojo) throws JsonRpcException;

	/**
	 * @param pojo The object to be serialized
	 * @return An array in JSON format in the same order as the definition of the
	 *         object properties.
	 * @throws JsonRpcException if an error occurs in serialization
	 */
	String serializeAsArray(Object pojo) throws JsonRpcException;

	/**
	 * @param <T>   The Class of the Object to be obtained.
	 * @param input The serialized object in JSON format.
	 * @param type  The class of the Object to be obtained.
	 * @return The Object represented by the input.
	 * @throws IOException if an error occurs in serialization
	 */
	<T> T deserialize(InputStream input, Class<T> type) throws IOException;
}
