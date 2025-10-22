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
import com.cibertec.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado " + username));
		if(user.getEnabled() == 0) {
			throw new DisabledException("Usuario deshabilitado");
		}else {
			return new CustomUserDetails(user);
		}
	}
	
	@Bean
    CommandLineRunner initUser() {
        return args -> {
            if (userRepository.findByUsername("marco").isEmpty()) {
                User user = new User();
                user.setUsername("marco");
                user.setEmail("marco@gmail.com");
                user.setPassword(passwordEncoder.encode("secret")); // importante: encriptar
                user.setEnabled((short) 1);
                Role roleUser = new Role();
                roleUser.setId(1);
                roleUser.setName("ADMIN");
                user.setRoles(Set.of(roleUser));

                userRepository.save(user);
                System.out.println("Usuario inicial creado: marco / secret");
            }
        };
    }
	
}
