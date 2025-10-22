package com.cibertec.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.entity.ToDo;
import com.cibertec.service.ToDoService;
import com.cibertec.utils.ApiResponse;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

	@Autowired
	private ToDoService todoService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> findAll(){
		List<ToDo> todo = todoService.findAll();
		if(todo == null || todo.isEmpty()) {
			return ApiResponse.error("Lista Vacia", HttpStatus.NO_CONTENT);
		}
		return ApiResponse.success(todo, "Lista obtenida", HttpStatus.OK);
	}
}
