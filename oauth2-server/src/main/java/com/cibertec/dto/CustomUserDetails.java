package com.cibertec.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cibertec.entity.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User{

	/**
	 * serialVersionUID es un identificador único para la serialización de objetos.
	 */
	private static final long serialVersionUID = 1L;
	
	private final String username;
	private final String email;
	
	public CustomUserDetails(User user) {
		//primer parametro deberia ser el username, pero en este caso usamos el id del usuario
		super(user.getUsername(), user.getPassword(), user.getRoles().stream().map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getName())).toList());
		this.username = user.getUsername();
		this.email = user.getEmail();
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}

}
