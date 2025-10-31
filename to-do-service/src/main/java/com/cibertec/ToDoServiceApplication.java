package com.cibertec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ToDoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoServiceApplication.class, args);
	}

}
