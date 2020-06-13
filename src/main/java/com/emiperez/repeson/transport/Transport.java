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
import java.util.concurrent.CompletableFuture;

/**
 * JSON-RPC does not depend on any specific transport. Any transport should
 * implement this interface, which allows to send JSON-RPC requests either
 * synchronous or asynchronous.
 */
public interface Transport {

	/**
	 * Send an synchronous JSON-RPC Request and get the Response as an InputStream.
	 * 
	 * @param jsonRpcRequest the JSON-RPC Request
	 * @return the JSON-RPC Response
	 * @throws IOException          if an I/O error occurs when sending or receiving
	 * @throws InterruptedException
	 */
	InputStream send(String jsonRpcRequest) throws IOException, InterruptedException;

	/**
	 * Send an asynchronous JSON-RPC Request and get the Response as an InputStream.
	 * <p>
	 * The returned completable future, if completed successfully, completes with an
	 * {@link InputStream} that contains the JSON-RPC Response.
	 * 
	 * @param jsonRpcRequest the JSON-RPC Request
	 * @return the JSON-RPC Response
	 */
	CompletableFuture<InputStream> sendAsync(String jsonRpcRequest);

}
