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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpTransport implements Transport {

	private HttpClient httpClient;
	private URI uri = URI.create("http://127.0.0.1:8080");
	private String contentType = "application/json";

	public static class Builder {

		private HttpClient httpClient;
		private URI uri = URI.create("http://127.0.0.1:8080");
		private String contentType = "application/json";

		private Builder() {
		}

		public Builder(HttpClient httpClient) {
			this();
			this.httpClient = httpClient;
		}

		public Builder uri(URI uri) {
			this.uri = uri;
			return this;
		}

		public Builder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public HttpTransport build() {
			HttpTransport transport = new HttpTransport();
			transport.httpClient = httpClient;
			transport.uri = uri;
			transport.contentType = contentType;
			return transport;
		}

	}

	public static Builder builder(HttpClient httpClient) {
		return new Builder(httpClient);
	}

	@Override
	public InputStream send(String rpcJsonRequest) throws IOException, InterruptedException {
		System.out.println("command: " + rpcJsonRequest);
		HttpRequest request = buildHttpRequest(rpcJsonRequest);
		HttpResponse<InputStream> response;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
		return response.body();
	}

	@Override
	public CompletableFuture<InputStream> sendAsync(String rpcJsonRequest)  {
		return httpClient.sendAsync(buildHttpRequest(rpcJsonRequest), HttpResponse.BodyHandlers.ofInputStream())
				.thenApply(r -> r.body());
	}

	private HttpRequest buildHttpRequest(String rpcJsonRequest) {
		return HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(rpcJsonRequest)).uri(uri)
				.header("Content-Type", contentType).build();
	}

}
