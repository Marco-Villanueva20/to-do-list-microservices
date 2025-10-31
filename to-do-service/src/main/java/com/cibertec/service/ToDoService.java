package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.dto.ToDoDTO;
import com.cibertec.entity.ToDo;
import com.cibertec.entity.User;
import com.cibertec.mapper.ToDoMapper;
import com.cibertec.repository.ToDoRepository;
import com.cibertec.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ToDoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToDoRepository todoRepository;

    // Nuevo método para ser usado en el Controller para comprobaciones (devuelve
    // Optional)
    public Optional<ToDo> findById(Long id) {
        return todoRepository.findById(id);
    }

    @Cacheable(value = "todos", key = "#id")
    public ToDoDTO getToDoByIdDto(Long id) {
        return todoRepository.findById(id)
                .map(ToDoMapper::toDto)
                .orElse(null);
    }

    @Transactional
    @CachePut(value = "todos", key = "#id")
    public ToDoDTO updateToDoDto(Long id, ToDo updated) {
        ToDo existing = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ToDo no encontrado: " + id));

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompleted(updated.getCompleted());
        ToDo saved = todoRepository.save(existing);
        return ToDoMapper.toDto(saved);
    }

    @Transactional
    @CachePut(value = "todos", key = "#result.id")
    public ToDoDTO createToDoDto(ToDo todo) {
        ToDo saved = todoRepository.save(todo);
        return ToDoMapper.toDto(saved);
    }

    // Maneja el Optional devuelto por JpaRepository
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @CacheEvict(value = "todos", key = "#id")
    public void deleteToDoById(Long id) {
        todoRepository.deleteById(id);
    }

    public List<ToDo> findAll() {
        return todoRepository.findAll();
    }

    // Llama al método de guardado principal
    @CircuitBreaker(name = "recommendationService", fallbackMethod = "fallbackCreateToDo")
    @Retry(name = "recommendationService")
    public ToDo createToDo(ToDo todo) {
        return this.createToDo(todo);
    }

    public ToDo fallbackCreateToDo(ToDo todo, Throwable ex) {
        return ToDo.builder().title(todo.getTitle()).description("No se pudo guardar. Intente más tarde.")
                .completed(false).build();
    }

    // Se mantiene el método findToDoById con CB/Retry, que ahora usa findById
    @CircuitBreaker(name = "recommendationService", fallbackMethod = "fallbackToDos")
    @Retry(name = "recommendationService")
    public ToDo findToDoById(Long id) {
        Optional<ToDo> todo = todoRepository.findById(id);
        return todo.get();
    }

    public ToDo fallbackToDos(Long id, Throwable ex) {
        return ToDo.builder().id(id).title("No disponible temporalmente")
                .description("Servicio fuera de línea, mostrando datos simulados").completed(false).build();
    }

}