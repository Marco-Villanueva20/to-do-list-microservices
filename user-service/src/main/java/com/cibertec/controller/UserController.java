package com.cibertec.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.entity.User;
import com.cibertec.service.UserService;
import com.cibertec.utils.ApiResponse;

@RestController
@RequestMapping("/api/user/")
public class UserController {

	@Autowired
	private UserService userService;

	public ResponseEntity<Map<String, Object>> create(User user) {
		if (user.getId() != null && userService.existsById(user.getId())) {
			return ApiResponse.error("Ya existe un usuario con este ID", HttpStatus.CONFLICT);
		}
		return userService.create(user);
	}
}
