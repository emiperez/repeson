package com.emiperez.commons.json.apiselector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.emiperez.repeson.client.JsonRpcException;

import lombok.extern.java.Log;

@Log
class JacksonJsonMapperTest {
	
	private static final JacksonJsonMapper mapper = new JacksonJsonMapper();
	private static Pojo pojo;

	@BeforeAll
	static void initPojo() {
		pojo = new Pojo();
		pojo.setId(1);
		pojo.setName("pojo");
		pojo.setList(IntStream.range(1, 3).boxed().collect(Collectors.toList()));
	}
	
	@Test
	void testSerialize() throws JsonRpcException {
		String json = mapper.serialize(pojo);
		log.info(json);
		assertEquals(json, "{\"id\":1,\"name\":\"pojo\",\"list\":[1,2]}");
	}

	@Test
	void testSerializeAsArray() throws JsonRpcException {
		String json = mapper.serializeAsArray(pojo);
		log.info(json);
		assertEquals(json, "[1,\"pojo\",[1,2]]");
	}

	@Test
	void testDeserialize() throws IOException {
		Pojo dPojo = mapper.deserialize("{\"id\":1,\"name\":\"pojo\",\"list\":[1,2]}", Pojo.class);
		assertEquals(pojo, dPojo);
	}
	
	

}
