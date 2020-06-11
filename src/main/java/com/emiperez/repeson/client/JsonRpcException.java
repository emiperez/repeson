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

/**
 * Wrapper for Exceptions occurred during a send of a request.
 * @author emiperez
 * @since 0.1
 */
public class JsonRpcException extends Exception {

	public JsonRpcException(String message) {
		super(message);
	}
	
	public JsonRpcException(String message, Throwable cause) {
		super(message, cause);
	}
}
