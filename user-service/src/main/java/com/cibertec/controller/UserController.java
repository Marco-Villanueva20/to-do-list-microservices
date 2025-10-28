package com.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.entity.User;
import com.cibertec.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	public ResponseEntity<User> create(User user) {
		return ResponseEntity.ok(userService.create(user));
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> createUser(@RequestBody User user){
		return ResponseEntity.ok(userService.create(user));
	}
	
	
}
