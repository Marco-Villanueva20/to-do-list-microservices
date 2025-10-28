package com.cibertec.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cibertec.entity.User;
import com.cibertec.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	public boolean existsById(Integer id) {
		return userRepository.existsById(id);
	}

	public List<User> getUsers() {
		List<User> usuarios = userRepository.findAll();
		return usuarios;

	}

	public User create(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User newUser = userRepository.save(user);
		return newUser;
	}
}
