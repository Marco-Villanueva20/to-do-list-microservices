package com.cibertec.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.dto.ToDoDTO;
import com.cibertec.entity.ToDo;
import com.cibertec.entity.User;
import com.cibertec.service.ToDoService;
import com.cibertec.utils.ApiResponse;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

	@Autowired
	private ToDoService todoService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> findAll() {
		List<ToDo> todo = todoService.findAll();
		if (todo == null || todo.isEmpty()) {
			return ApiResponse.error("Lista Vacia", HttpStatus.NO_CONTENT);
		}
		return ApiResponse.success(todo, "Lista obtenida", HttpStatus.OK);
	}

	 @GetMapping("/{id}")
    public ResponseEntity<ToDoDTO> getToDoById(@PathVariable Long id) {
        ToDoDTO dto = todoService.getToDoByIdDto(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ToDoDTO> createToDo(
            @RequestBody ToDo todo,
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();
        User user = todoService.findUserByUsername(username);
        if (user == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        todo.setUser(user);
        ToDoDTO createdDto = todoService.createToDoDto(todo);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoDTO> updateToDo(@PathVariable Long id, @RequestBody ToDo todo) {
        // valida existencia si quieres
        try {
            ToDoDTO updated = todoService.updateToDoDto(id, todo);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        Optional<ToDo> maybe = todoService.findById(id);
        if (maybe.isEmpty()) return ResponseEntity.notFound().build();
        todoService.deleteToDoById(id);
        return ResponseEntity.ok().build();
    }
}