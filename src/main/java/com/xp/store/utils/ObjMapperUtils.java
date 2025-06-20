package com.xp.store.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjMapperUtils {

	
	public static ObjectMapper objectMapper;
	
	
	@SuppressWarnings("static-access")
	public ObjMapperUtils(ObjectMapper objectMapper){
		this.objectMapper = objectMapper;
	}

	public static void printObject(Object object) {
		System.out.println(objectToString(object));
	}

	public static String objectToString(Object object) {
		try {
			String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object jsonToObject(String json, Class<?> objectClass) {
		try {
			Object obj = objectMapper.readValue(json,objectClass);
			return obj;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
