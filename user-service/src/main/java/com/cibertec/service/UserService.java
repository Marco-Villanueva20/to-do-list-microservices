package com.cibertec.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cibertec.entity.User;
import com.cibertec.repository.UserRepository;
import com.cibertec.utils.ApiResponse;
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public boolean existsById(Integer id) {
		return userRepository.existsById(id);
	}
	
	public ResponseEntity<Map<String, Object>> findAll(){
		  List<User> usuarios = userRepository.findAll();
		    if (usuarios.isEmpty()) {
		        return ApiResponse.error("No hay usuarios registrados", HttpStatus.NOT_FOUND);
		    }
		    return ApiResponse.success(usuarios, "Lista obtenida correctamente", HttpStatus.OK);
	}
	
	public ResponseEntity<Map<String, Object>> create(User user){
		
		User newUser = userRepository.save(user);
		return ApiResponse.success(newUser, "Usuario creado correctamente",HttpStatus.CREATED);
	}
}
