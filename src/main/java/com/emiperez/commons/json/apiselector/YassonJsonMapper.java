package com.emiperez.commons.json.apiselector;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import lombok.extern.java.Log;

@Log
public class YassonJsonMapper implements JsonMapper {

	private Jsonb jsonb = JsonbBuilder.create();

	private Jsonb paramsJsonB = JsonbBuilder.create(new JsonbConfig().withSerializers(new JsonbSerializer<Object>() {

		@Override
		public void serialize(Object obj, JsonGenerator generator, SerializationContext ctx) {
			generator.writeStartArray();
			for (Field field : obj.getClass().getDeclaredFields()) {
				Object value = null;
				try {
					field.setAccessible(true);
					value = field.get(obj);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(value == null) {
					generator.write("");
				} else if (value instanceof Integer) {
					generator.write((int) value);					
				} else if (value instanceof Boolean) {
					generator.write((boolean) value);					
				} else {
					generator.write(value.toString());
				}
			}
			generator.writeEnd();
		}

	}));

	@Override
	public String serialize(Object object) {
		return jsonb.toJson(object);
	}

	@Override
	public String serializeAsArray(Object object) {
		return paramsJsonB.toJson(object);
	}

	@Override
	public <T> T deserialize(InputStream input, Class<T> type) {

			String text = null;
		    try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())) {
		        text = scanner.useDelimiter("\\A").next();
		        log.info("RESPONSE: " + text);
		    }

		return jsonb.fromJson(text, type);
	}

}
