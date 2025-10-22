package com.cibertec.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

	public static ResponseEntity<Map<String,Object>> success(Object data, String message,HttpStatus status){
		Map<String,Object> response = new HashMap<>();
		response.put("status", status.value());
		response.put("message", message);
		response.put("data", data);
		return ResponseEntity.ok(response);
	}
	
	public static ResponseEntity<Map<String, Object>> error(String message, HttpStatus status){
		Map<String,Object> response = new HashMap<>();
		response.put("status", status.value());
		response.put("message", message);
		response.put("data", null);
		return new ResponseEntity<>(response, status);
	}
}
