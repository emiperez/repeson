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

import java.text.MessageFormat;

import com.emiperez.commons.json.apiselector.Json;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

/**
 * A JSON-RPC request.
 *
 * <p> An {@code JsonRpcRequest} instance is built through a builder.
 * Once all required parameters have been set in the builder, {@link
 * JsonRpcRequest.Builder#build() build} will return the {@code JsonRpcRquest}.
 *
 * @since 0.1
 */

@Log
@Getter
public class JsonRpcRequest{	

	private static final String RPC_JSON_TEMPLATE = "'{'\"jsonrpc\": \"{0}\", \"id\": \"{1}\", \"method\": \"{2}\"{3}'}'";

	private JsonRpcVersion jsonrpc = JsonRpcVersion.v2_0;
	@NonNull
	private String id;
	@NonNull
	private String method;
	private Object params;
	private boolean isNamedParams;
	
	public static class Builder {
		private JsonRpcVersion jsonrpc = JsonRpcVersion.v2_0;
		@NonNull
		private String id;
		@NonNull
		private String method;
		private Object params;
		private boolean isNamedParams = true;
		
		public Builder jsonrpc(JsonRpcVersion jsonrpc) {
			this.jsonrpc = jsonrpc;
			return this;
		}
		
		public Builder id(String id) {
			this.id = id;
			return this;			
		}
		
		public Builder method(String method) {
			this.method = method;
			return this;
		}
		
		public Builder params(Object params) {
			this.params = params;
			return this;
		}
		
		public Builder isNamedParams(boolean isNamedParams) {
			this.isNamedParams = isNamedParams;
			return this;
		}
		
		public JsonRpcRequest build() {
			JsonRpcRequest request = new JsonRpcRequest();
			request.jsonrpc = jsonrpc;
			request.id = id;
			request.method = method;
			request.params = params;
			request.isNamedParams = isNamedParams;
			return request;
		}
	}
	
	/**
	 * 
	 * @param namedParams
	 * @return
	 * @throws JsonRpcException
	 */
	public String getJson() throws JsonRpcException {
		String paramsJson = "";
		if(params != null) {
			paramsJson = ", \"params\": " + Json.INSTANCE.api().serialize(params, isNamedParams);			
		}
		String sRequest = MessageFormat.format(RPC_JSON_TEMPLATE, jsonrpc, id, method, paramsJson);
		log.info("Request: " + sRequest);
		return sRequest;
	}
	

	public static Builder builder() {
		return new Builder();
	}
}
