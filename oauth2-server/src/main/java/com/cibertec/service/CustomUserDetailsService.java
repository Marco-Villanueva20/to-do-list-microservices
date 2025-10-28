package com.cibertec.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibertec.dto.CustomUserDetails;
import com.cibertec.entity.Role;
import com.cibertec.entity.User;
import com.cibertec.repository.RoleRepository;
import com.cibertec.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado " + username));
		if(user.getEnabled() == 0) {
			throw new DisabledException("Usuario deshabilitado");
		}else {
			return new CustomUserDetails(user);
		}
	}
	
	// Código corregido para el CommandLineRunner
@Bean
CommandLineRunner initUser() {
    return args -> {
        
        // **1. Crear y Persistir el Rol primero si no existe**
        Role adminRole = roleRepository.findByName("ADMIN")
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ADMIN");
                return roleRepository.save(newRole); // ¡Importante: guardar primero!
            });

        // 2. Crear y Persistir el Usuario (usando el rol persistido)
        if (userRepository.findByUsername("marco").isEmpty()) {
            User user = new User();
            user.setUsername("marco");
            user.setEmail("marco@gmail.com");
            user.setPassword(passwordEncoder.encode("secret"));
            user.setEnabled((short) 1);
            
            // Asignar el rol que ya existe en la DB
            user.setRoles(Set.of(adminRole)); 

            userRepository.save(user);
            System.out.println("Usuario inicial creado: marco / secret");
        }
    };
}
	
}
