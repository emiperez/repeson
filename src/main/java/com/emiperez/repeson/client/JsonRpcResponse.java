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
import java.io.InputStream;

import com.emiperez.repeson.mapper.Json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonRpcResponse<T> {

	private T result;
	private JsonRpcResponseError error;
	private String id;

	public static <C> JsonRpcResponse<C> of(InputStream input) throws IOException {
			return Json.INSTANCE.api().deserialize(input, JsonRpcResponse.class);
	}

	public static <R extends JsonRpcResponse<C>, C> R of(InputStream input, Class<R> type) throws IOException {
		return Json.INSTANCE.api().deserialize(input, type);
	}
}
