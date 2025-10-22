package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.entity.ToDo;
import com.cibertec.repository.ToDoRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ToDoService {

	@Autowired
	private ToDoRepository todoRepository;

	public List<ToDo> findAll() {
		return todoRepository.findAll();
	}

	@CircuitBreaker(name = "recommendationService", fallbackMethod = "fallbackCreateToDo")
	@Retry(name="recommendationService")
	public ToDo createToDo(ToDo todo) {
		return todoRepository.save(todo);
	}

	// Fallback correspondiente
	public ToDo fallbackCreateToDo(ToDo todo, Throwable ex) {
		return ToDo.builder().title(todo.getTitle()).description("No se pudo guardar. Intente más tarde.")
				.completed(false).build();
	}

	@CircuitBreaker(name = "recommendationService", fallbackMethod = "fallbackToDos")
	@Retry(name="recommendationService")
	public ToDo findToDoById(Long id) {
		Optional<ToDo> todo = todoRepository.findById(id);
		return todo.get();
	}

	// Fallback correspondiente
	public ToDo fallbackToDos(Long id, Throwable ex) {
		return ToDo.builder().id(id).title("No disponible temporalmente")
				.description("Servicio fuera de línea, mostrando datos simulados").completed(false).build();
	}

}
