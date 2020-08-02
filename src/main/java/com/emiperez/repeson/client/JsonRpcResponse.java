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
import java.util.function.Consumer;

import com.emiperez.commons.json.apiselector.Json;

import lombok.Getter;
import lombok.Setter;

/**
 * A JSON-RPC Response. InputStreams that have JSON-RPC Response structure can
 * be parsed to an Object of this Class by using the two static methods
 * included.
 *
 * @param <T> The Type of the result of the JSON-RPC response.
 */
@Getter
@Setter
public class JsonRpcResponse<T> {

	private T result;
	private JsonRpcResponseError error;
	private String id;

	/**
	 * Static method for parsing JSON-RPC Response messages whose result classes do
	 * not use Generics
	 * 
	 * @param <C>   The Type of the result property
	 * @param input The InputStream of a JSON-RPC Response message
	 * @return JsonRpcResponse whose result type does not use generics
	 * @throws IOException if an error occurs in deserialization
	 */
	public static <C> JsonRpcResponse<C> of(InputStream input) throws IOException  {
		return Json.INSTANCE.api().deserialize(input, JsonRpcResponse.class);
	}

	/**
	 * Static method for parsing JSON-RPC Response messages whose result classes do
	 * need the use of Generics To prevent the Type Erasure, a class file that
	 * extends JsonRpcResponse must be created.
	 * 
	 * @param <R>   Class that extends JsonRpcResponse to prevent the Type Erasure
	 * @param <C>   The Type of the Result
	 * @param input The InputStream of a JSON-RPC Response message
	 * @param type  Class that extends JsonRpcResponse to prevent the Type Erasure
	 * @return The parsed JSON-RPC response as an Object of the Class that extends
	 *         JsonRpcResponse
	 * @throws IOException if an error occurs in deserialization
	 */
	public static <R extends JsonRpcResponse<C>, C> R of(InputStream input, Class<R> type) throws IOException {
		return Json.INSTANCE.api().deserialize(input, type);
	}
	
	/**
	 * 
	 * @return true if this response has a result object. False otherwise.
	 */
	public boolean hasResult() {
		return result != null;
	}
	
	/**
	 * 
	 * @return true if this response has an Error object, false otherwise.
	 */
	public boolean hasError() {
		return error != null;
	}
	
    /**
     * If a result is present, performs the given action with the result,
     * otherwise does nothing.
     *
     * @param action the action to be performed, if a result is present
     * @throws NullPointerException if result is present and the given action is
     *         {@code null}
     */
    public void ifHasResult(Consumer<? super T> action) {
        if (result != null) {
            action.accept(result);
        }
    }
    
    /**
     * If a result is present, performs the given action with the result,
     * otherwise performs the given empty-based action.
     *
     * @param action the action to be performed, if a result is present
     * @param emptyAction the empty-based action to be performed, if no result is
     *        present
     * @throws NullPointerException if a result is present and the given action
     *         is {@code null}, or no result is present and the given empty-based
     *         action is {@code null}.
     */
    public void ifHasResultOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (result != null) {
            action.accept(result);
        } else {
            emptyAction.run();
        }
    }
	
    /**
     * If an error is present, performs the given action with the error,
     * otherwise does nothing.
     *
     * @param action the action to be performed, if an error is present
     * @throws NullPointerException if error is present and the given action is
     *         {@code null}
     */
    public void ifHasError(Consumer<? super JsonRpcResponseError> action) {
        if (error != null) {
            action.accept(error);
        }
    }
    
    /**
     * If a result is present, performs the given action with the result,
     * otherwise performs the given empty-based action.
     *
     * @param action the action to be performed, if an error is present
     * @param emptyAction the empty-based action to be performed, if no result is
     *        present
     * @throws NullPointerException if an error is present and the given action
     *         is {@code null}, or no result is present and the given empty-based
     *         action is {@code null}.
     */
    public void ifHasErrorOrElse(Consumer<? super JsonRpcResponseError> action, Runnable emptyAction) {
        if (error != null) {
            action.accept(error);
        } else {
            emptyAction.run();
        }
    }
}
