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
 * @param <R>
 * @param <T>
 * @since 0.1
 */

@Builder
public class JsonRpcClient {

	private IdGenerator<?> idGenerator;
	private Transport transport;
	private JsonRpcVersion version;
	
	public static class JsonRpcClientBuilder {
		
		public JsonRpcClientBuilder isNamedParams(boolean isNamedParams) {
			if (isNamedParams) {
			}
			return this;
		}
	}

	public JsonRpcVersion getVersion() {
		return version;
	}

	public <T> JsonRpcResponse<T> send(JsonRpcRequest request) throws IOException, InterruptedException, JsonRpcException  {
		return  JsonRpcResponse.of(transport.send(request.getJson()));
	}
	
	public <V extends JsonRpcResponse<C>, C> V send(JsonRpcRequest request, Class<V> type) throws IOException, InterruptedException, JsonRpcException  {
		return  JsonRpcResponse.of(transport.send(request.getJson()), type);
	}

	public <T> CompletableFuture<JsonRpcResponse<T>> sendAsync(JsonRpcRequest request) throws JsonRpcException {
		return transport.sendAsync(request.getJson()).thenApply(r -> {
			try {
				return JsonRpcResponse.of(r);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});		
	}

	public <R extends JsonRpcResponse<T>, T> CompletableFuture<R> sendAsync(JsonRpcRequest request, Class<R> type) throws JsonRpcException {
		return transport.sendAsync(request.getJson()).thenApply(r -> {
			try {
				return JsonRpcResponse.of(r, type);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	public <T> JsonRpcResponse<T> sendRequestWithDefaults(String method) throws IOException, InterruptedException, JsonRpcException {
		return sendRequestWithDefaults(method, null);
	}

	public <T> JsonRpcResponse<T> sendRequestWithDefaults(String method, Object params) throws IOException, InterruptedException, JsonRpcException  {
		JsonRpcResponse<T> response = send(buildRequest(method, params));
		return response;
	}

	public <R extends JsonRpcResponse<T>, T> R sendRequestWithDefaults(String method, Object params, Class<R> type) throws IOException, InterruptedException, JsonRpcException
			 {
		return send(buildRequest(method, params), type);
	}

	public <T> CompletableFuture<JsonRpcResponse<T>> sendRequestWithDefaultsAsync(String method) throws JsonRpcException {
		return sendRequestWithDefaultsAsync(method, null);
	}

	public <T> CompletableFuture<JsonRpcResponse<T>> sendRequestWithDefaultsAsync(String method, Object params)
			throws JsonRpcException {
		return sendAsync(buildRequest(method, params));
	}

	public <R extends JsonRpcResponse<T>, T> CompletableFuture<R> sendRequestWithDefaultsAsync(String method, Object params, Class<R> type)
			throws JsonRpcException {
		return sendAsync(buildRequest(method, params), type);
	}	

	private JsonRpcRequest buildRequest(String method, Object params) {
		return JsonRpcRequest.builder().id("" + idGenerator.getId()).jsonrpc(version)
				.method(method).params(params).isNamedParams(false).build();
	}	
}