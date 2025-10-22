package com.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.entity.ToDo;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

}
