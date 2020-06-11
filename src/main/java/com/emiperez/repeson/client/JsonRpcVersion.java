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

public enum JsonRpcVersion { 
	v1_0 ("1.0"), v1_1("1.1"), v1_2("1.2"), v2_0("2.0");
	
	private String versionCode;
	JsonRpcVersion (String versionCode) {
		this.versionCode = versionCode;
	}
	
	public String toString() {
		return versionCode;
	}
}
