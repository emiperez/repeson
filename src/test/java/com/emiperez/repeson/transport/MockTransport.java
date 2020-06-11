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

package com.emiperez.repeson.transport;

import static java.util.Map.entry;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.emiperez.repeson.transport.Transport;

import lombok.extern.java.Log; 

@Log
public class MockTransport implements Transport {
	
	
	private static final Map<String, String> responses = Map.ofEntries(
				entry("{\"jsonrpc\": \"2.0\", \"id\": \"1\", \"method\": \"getinteger\"}", 
						"{\"result\": 19, \"id\": 1, \"error\": null}"),
				entry("{\"jsonrpc\": \"2.0\", \"id\": \"2\", \"method\": \"getintegers\"}", 
						"{\"result\": [19, 4, 7], \"id\": 2, \"error\": null}"),
				entry("{\"jsonrpc\": \"2.0\", \"id\": \"3\", \"method\": \"geterror\"}", 
						"{\"result\":null,\"error\":{\"code\":-1,\"message\":\"Error Message\"},\"id\": 3}"),
				entry("{\"jsonrpc\": \"2.0\", \"id\": \"4\", \"method\": \"getintegers\", \"params\": {\"page\":1,\"rows\":10}}", 
						"{\"result\": [1,2,3,4,5,6,7,8,9,10], \"id\": 4}")
			);

	@Override
	public InputStream send(String request) {
		String response = responses.get(request);
		log.info("RESPONSE: " + response);
		return new ByteArrayInputStream(response.getBytes());
	}

	@Override
	public CompletableFuture<InputStream> sendAsync(String request) {
		return CompletableFuture.completedFuture(send(request));
	}

}
