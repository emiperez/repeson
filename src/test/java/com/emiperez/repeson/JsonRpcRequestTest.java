package com.emiperez.repeson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.emiperez.repeson.client.JsonRpcException;
import com.emiperez.repeson.client.JsonRpcRequest;

import lombok.Getter;

class JsonRpcRequestTest {

	@Test
	void whenNoNamedParamsNoNamesTest() throws JsonRpcException {
		
		@Getter
		class Params {
			private int page = 3;
			private int rows = 7;
		}
		
		JsonRpcRequest request = JsonRpcRequest.builder()
				.id("a")
				.method("b")
				.params(new Params())
				.isNamedParams(false)
				.build();
		assertEquals(request.getJson(), 
				"{\"jsonrpc\": \"2.0\", \"id\": \"a\", \"method\": \"b\", \"params\": [3,7]}");
	}

	@Test
	void whenNamedParamsNamesTest() throws JsonRpcException {
		
		@Getter
		class Params {
			private int page = 3;
			private int rows = 7;
		}
		
		JsonRpcRequest request = JsonRpcRequest.builder()
				.id("a")
				.method("b")
				.params(new Params())
				.isNamedParams(true)
				.build();
		assertEquals(request.getJson(), 
				"{\"jsonrpc\": \"2.0\", \"id\": \"a\", \"method\": \"b\", \"params\": {\"page\":3,\"rows\":7}}");
	}

}
