/*
 * Copyright (c) 2020 Emilio Perez. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code)
 */

package com.emiperez.repeson.client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

import com.emiperez.commons.idgenerators.IdGenerator;
import com.emiperez.repeson.transport.Transport;

import lombok.Builder;

/**
 * A JsonRpc Client.
 *
 * <p>
 * A {@code JsonRpcClient} can be used to send {@linkplain JsonRpcRequest
 * requests} and retrieve their {@linkplain JsonRpcResponse responses}. An
 * {@code
 * JsonRpcClient} is created through a {@link JsonRpcClient#builder() builder}.
 * The builder can be used to configure per-client state, like: the preferred
 * protocol version, preferred {@linkplain Transport transport} or whether it
 * named parameters are preferred. Once built, a {@code JsonRpcClient} can be
 * used to send multiple requests.
 *
 * <p>
 * Requests can be sent either synchronously or asynchronously:
 * <ul>
 * <li>{@link JsonRpcClient#send(JsonRpcRequest)} blocks until the request has
 * been sent and the response has been received.</li>
 *
 * <li>{@link JsonRpcClient#sendAsync(JsonRpcRequest)} sends the request and
 * receives the response asynchronously. The {@code sendAsync} method returns
 * immediately with a {@link CompletableFuture
 * CompletableFuture}&lt;{@link JsonRpcResponse}&gt;. The {@code
 *     CompletableFuture} completes when the response becomes available. The
 * returned {@code CompletableFuture} can be combined in different ways to
 * declare dependencies among several asynchronous tasks.</li>
 * </ul>
 * 
 * @since 0.1
 */

@Builder
public class JsonRpcClient {

	private IdGenerator<?> idGenerator;
	private Transport transport;
	@Builder.Default
	private JsonRpcVersion version = JsonRpcVersion.v2_0;

	public static class JsonRpcClientBuilder {

		public JsonRpcClientBuilder isNamedParams(boolean isNamedParams) {
			if (isNamedParams) {
			}
			return this;
		}
	}

	/**
	 * Returns the preferred JSON-RPC protocol version for this client. The default
	 * value is {@link JsonRpcVersion#v2_0}
	 *
	 * @return the JSON-RPC protocol version requested
	 */
	public JsonRpcVersion getVersion() {
		return version;
	}

	/**
	 * Sends the given {@link JsonRpcRequest} using this client. Returns a
	 * {@link JsonRpcResponse}{@code <T>}
	 * 
	 * @param <T>     the type of the result that the response should include
	 * @param request the request
	 * @return the response
	 * @throws IOException          if an I/O error occurs when sending or receiving
	 * @throws InterruptedException if the operation is interrupted
	 * @throws JsonRpcException     if an error occurs in the serialization of the
	 *                              request
	 */
	public <T> JsonRpcResponse<T> send(JsonRpcRequest request)
			throws IOException, InterruptedException, JsonRpcException {
		return JsonRpcResponse.of(transport.send(request.getJson()));
	}

	/**
	 * Sends the given {@link JsonRpcRequest} using this client. Returns an Object
	 * of a Class Type {@code <R>} that extends {@link JsonRpcResponse}{@code <T>}
	 * to prevent Type Erasure. This Class Type must be passed as an argument.
	 * 
	 * @param <R>     the Class that extends JsonRpcResponse
	 * @param <T>     the type of the result that the response should include
	 * @param request the request
	 * @param type    the Class that extends JsonRpcResponse
	 * @return the response
	 * @throws IOException          if an I/O error occurs when sending or receiving
	 * @throws InterruptedException if the operation is interrupted
	 * @throws JsonRpcException     if an error occurs in the serialization of the
	 *                              request
	 */
	public <R extends JsonRpcResponse<T>, T> R send(JsonRpcRequest request, Class<R> type)
			throws IOException, InterruptedException, JsonRpcException {
		return JsonRpcResponse.of(transport.send(request.getJson()), type);
	}

	/**
	 * Sends the given request asynchronously using this client.
	 * 
	 * @param <T>     the type of the result that the response should include
	 * @param request the request
	 * @return a {@code CompletableFuture<JsonRpcResponse<T>>}
	 * @throws JsonRpcException if an error occurs in the serialization of the
	 *                          request
	 */
	public <T> CompletableFuture<JsonRpcResponse<T>> sendAsync(JsonRpcRequest request) throws JsonRpcException {
		return transport.sendAsync(request.getJson()).thenApply(r -> {
			try {
				return JsonRpcResponse.of(r);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	/**
	 * Sends the given request asynchronously using this client. Returns a
	 * CompletableFuture, whose Type Parameter is a Class that extends
	 * {@link JsonRpcResponse}{@code <T>} to prevent Type Erasure. This Class Type
	 * must be passed as an argument.
	 * 
	 * @param <R>     the Class that extends JsonRpcResponse
	 * @param <T>     the type of the result that the response should include
	 * @param request the request
	 * @param type    the Class that extends JsonRpcResponse
	 * @return a <{@code CompletableFuture<R>}
	 * @throws JsonRpcException if an error occurs in the serialization of the
	 *                          request
	 */
	public <R extends JsonRpcResponse<T>, T> CompletableFuture<R> sendAsync(JsonRpcRequest request, Class<R> type)
			throws JsonRpcException {
		return transport.sendAsync(request.getJson()).thenApply(r -> {
			try {
				return JsonRpcResponse.of(r, type);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	/**
	 * Creates and sends a JSON-RPC Request, that needs no parameter and whose
	 * method is passed as an argument, with this client. The id is obtained by the
	 * client's {@link IdGenerator}
	 * 
	 * @param <T>    the type of the result that the response should include
	 * @param method the method
	 * @return the response
	 * @throws IOException          if an I/O error occurs when sending or receiving
	 * @throws InterruptedException if the operation is interrupted
	 * @throws JsonRpcException     if an error occurs in the serialization of the
	 *                              request
	 */
	public <T> JsonRpcResponse<T> sendRequestWithDefaults(String method)
			throws IOException, InterruptedException, JsonRpcException {
		return sendRequestWithDefaults(method, null);
	}

	/**
	 * Creates and sends a JSON-RPC Request, whose method and params are passed as
	 * arguments, with this client. The id is obtained by the client's
	 * {@link IdGenerator}
	 * 
	 * @param <T>    the type of the result that the response should include
	 * @param method the method
	 * @param params a POJO with the method's parameters
	 * @return the response
	 * @throws IOException          if an I/O error occurs when sending or receiving
	 * @throws InterruptedException if the operation is interrupted
	 * @throws JsonRpcException     if an error occurs in the serialization of the
	 *                              request
	 */
	public <T> JsonRpcResponse<T> sendRequestWithDefaults(String method, Object params)
			throws IOException, InterruptedException, JsonRpcException {
		JsonRpcResponse<T> response = send(buildRequest(method, params));
		return response;
	}

	/**
	 * Creates and sends a JSON-RPC Request, whose method and params are passed as
	 * arguments, with this client. The id is obtained by the client's
	 * {@link IdGenerator}. Returns an Object of a Class Type {@code <R>} that
	 * extends {@link JsonRpcResponse}{@code <T>} to prevent Type Erasure. This
	 * Class Type must be passed as an argument.
	 * 
	 * @param <R>    the Class that extends JsonRpcResponse
	 * @param <T>    the type of the result that the response should include
	 * @param method the method
	 * @param params a POJO with the method's parameters
	 * @param type   the Class that extends JsonRpcResponse
	 * @return the response
	 * @throws IOException          if an I/O error occurs when sending or receiving
	 * @throws InterruptedException if the operation is interrupted
	 * @throws JsonRpcException     if an error occurs in the serialization of the
	 *                              request
	 */
	public <R extends JsonRpcResponse<T>, T> R sendRequestWithDefaults(String method, Object params, Class<R> type)
			throws IOException, InterruptedException, JsonRpcException {
		return send(buildRequest(method, params), type);
	}

	/**
	 * Creates and sends asynchronously a JSON-RPC Request, that needs no parameter
	 * and whose method is passed as an argument, with this client. The id is
	 * obtained by the client's {@link IdGenerator}
	 * 
	 * @param <T>    the type of the result that the response should include
	 * @param method the method
	 * @return a {@code CompletableFuture<JsonRpcResponse<T>>}
	 * @throws JsonRpcException if an error occurs in the serialization of the
	 *                          request
	 */
	public <T> CompletableFuture<JsonRpcResponse<T>> sendRequestWithDefaultsAsync(String method)
			throws JsonRpcException {
		return sendRequestWithDefaultsAsync(method, null);
	}

	/**
	 * Creates and sends asynchronously a JSON-RPC Request, whose method and params
	 * are passed as arguments, with this client. The id is obtained by the client's
	 * {@link IdGenerator}
	 * 
	 * @param <T>    the type of the result that the response should include
	 * @param method the method
	 * @return a {@code CompletableFuture<JsonRpcResponse<T>>}
	 * @throws JsonRpcException if an error occurs in the serialization of the
	 *                          request
	 */
	public <T> CompletableFuture<JsonRpcResponse<T>> sendRequestWithDefaultsAsync(String method, Object params)
			throws JsonRpcException {
		return sendAsync(buildRequest(method, params));
	}

	/**
	 * Creates and sends asynchronously a JSON-RPC Request, whose method and params
	 * are passed as arguments, with this client. The id is obtained by the client's
	 * {@link IdGenerator}. Returns a CompletableFuture, whose Type Parameter is a
	 * Class that extends {@link JsonRpcResponse}{@code <T>} to prevent Type
	 * Erasure. This Class Type must be passed as an argument.
	 * 
	 * @param <R>    the Class that extends JsonRpcResponse
	 * @param <T>    the type of the result that the response should include
	 * @param method the method
	 * @param params a POJO with the method's parameters
	 * @param type   the Class that extends JsonRpcResponse
	 * @return a {@code CompletableFuture<JsonRpcResponse<T>>}
	 * @throws JsonRpcException if an error occurs in the serialization of the
	 *                          request
	 */
	public <R extends JsonRpcResponse<T>, T> CompletableFuture<R> sendRequestWithDefaultsAsync(String method,
			Object params, Class<R> type) throws JsonRpcException {
		return sendAsync(buildRequest(method, params), type);
	}

	private JsonRpcRequest buildRequest(String method, Object params) {
		return JsonRpcRequest.builder().id("" + idGenerator.getId()).jsonrpc(version).method(method).params(params)
				.isNamedParams(false).build();
	}
}