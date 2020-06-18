package com.emiperez.repeson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.emiperez.repeson.client.JsonRpcClient;
import com.emiperez.repeson.client.JsonRpcException;
import com.emiperez.repeson.client.JsonRpcRequest;
import com.emiperez.repeson.client.JsonRpcResponse;
import com.emiperez.repeson.client.JsonRpcResponseError;
import com.emiperez.repeson.client.JsonRpcVersion;
import com.emiperez.repeson.transport.MockTransport;
import com.emiperez.repeson.transport.Transport;

import lombok.Getter;
import lombok.extern.java.Log;

@TestInstance(Lifecycle.PER_CLASS)
@Log
public class JsonRpcClientTest {

	private JsonRpcClient client;

	@BeforeAll
	public void init() throws IOException {
		Transport transport = new MockTransport();
		client = JsonRpcClient.builder().version(JsonRpcVersion.v2_0).transport(transport).build();
	}

	@Test
	public void whenResultIntegerParseTest() throws IOException, InterruptedException, JsonRpcException  {
		JsonRpcRequest request = JsonRpcRequest.builder().id("1").jsonrpc(JsonRpcVersion.v2_0).method("getinteger")
				.build();
		JsonRpcResponse<Integer> response = client.send(request);
		assertTrue(response.hasResult());
		assertEquals(response.getResult(), 19);
		log.info("Id: " + response.getId());
		assertEquals(response.getId(), "1");
		assertFalse(response.hasError());
	}

	@Test
	public void whenResultListOfIntegerParseTest() throws IOException, InterruptedException, JsonRpcException  {
		JsonRpcRequest request = JsonRpcRequest.builder().id("2").jsonrpc(JsonRpcVersion.v2_0).method("getintegers")
				.build();
		JsonRpcResponse<JsonRpcResponseError> response = client.send(request);
		assertEquals(response.getResult(), new ArrayList<Integer>(List.of(19, 4, 7)));
		log.info("Id: " + response.getId());
		assertEquals(response.getId(), "2");
		assertFalse(response.hasError());
	}

	@Test
	public void whenErrorParseErrorTest() throws IOException, InterruptedException, JsonRpcException  {
		JsonRpcRequest request = JsonRpcRequest.builder().id("3").jsonrpc(JsonRpcVersion.v2_0).method("geterror")
				.build();
		JsonRpcResponse<Integer> response = client.send(request);
		assertFalse(response.hasResult());
		assertNull(response.getResult());
		log.info("Id: " + response.getId());
		assertEquals(response.getId(), "3");
		assertTrue(response.hasError());
		assertEquals(response.getError().getMessage(), "Error Message");
		assertEquals(response.getError().getCode(), -1);
	}

	@Test
	public void whenParamsIncludeParams() throws IOException, InterruptedException, JsonRpcException  {

		@Getter
		class Params implements Serializable {
			private int page = 1;
			private int rows = 10;
		}

		JsonRpcRequest request = JsonRpcRequest.builder().id("4").jsonrpc(JsonRpcVersion.v2_0).method("getintegers")
				.params(new Params()).isNamedParams(true).build();
		JsonRpcResponse<Integer> response = client.send(request);
		assertTrue(response.hasResult());
		assertEquals(response.getResult(), IntStream.range(1, 11).boxed().collect(Collectors.toList()));
		log.info("Id: " + response.getId());
		assertEquals(response.getId(), "4");
		assertFalse(response.hasError());
		assertNull(response.getError());
	}

	@Test
	public void whenAsyncAllOfTest() throws JsonRpcException, InterruptedException, ExecutionException
			 {
		JsonRpcRequest request1 = JsonRpcRequest.builder().id("1").jsonrpc(JsonRpcVersion.v2_0).method("getinteger")
				.build();
		CompletableFuture<JsonRpcResponse<Integer>> cresponse1 = client.sendAsync(request1);

		JsonRpcRequest request2 = JsonRpcRequest.builder().id("2").jsonrpc(JsonRpcVersion.v2_0).method("getintegers")
				.build();
		CompletableFuture<JsonRpcResponse<List<Integer>>> cresponse2 = client.sendAsync(request2);

		JsonRpcRequest request3 = JsonRpcRequest.builder().id("3").jsonrpc(JsonRpcVersion.v2_0).method("geterror")
				.build();
		CompletableFuture<JsonRpcResponse<Integer>> cresponse3 = client.sendAsync(request3);
		CompletableFuture.allOf(cresponse1, cresponse2, cresponse3).join();

		JsonRpcResponse<Integer> response1 = cresponse1.get();
		assertTrue(response1.hasResult());
		assertEquals(response1.getResult(), 19);
		log.info("Id: " + response1.getId());
		assertEquals(response1.getId(), "1");
		assertFalse(response1.hasError());
		assertNull(response1.getError());

		JsonRpcResponse<List<Integer>> response2 = cresponse2.get();
		assertTrue(response2.hasResult());
		assertEquals(response2.getResult(), new ArrayList<Integer>(List.of(19, 4, 7)));
		log.info("Id: " + response2.getId());
		assertEquals(response2.getId(), "2");
		assertFalse(response2.hasError());
		assertNull(response2.getError());

		JsonRpcResponse<Integer> response3 = cresponse3.get();
		assertFalse(response3.hasResult());
		assertNull(response3.getResult());
		log.info("Id: " + response3.getId());
		assertEquals(response3.getId(), "3");
		assertTrue(response3.hasError());
		assertEquals(response3.getError().getMessage(), "Error Message");
		assertEquals(response3.getError().getCode(), -1);
	}
}
