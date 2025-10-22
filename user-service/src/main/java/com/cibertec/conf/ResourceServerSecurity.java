package com.cibertec.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServerSecurity {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.GET, "/api/user/**").hasAuthority("SCOPE_read")
				.requestMatchers(HttpMethod.POST, "/api/user/**").permitAll()
				.anyRequest().authenticated()).oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		return http.build();
	}
     
	
	
	
	
	/*
	 * @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") private
	 * String jwkSetUri;
	 * 
	 * @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws
	 * Exception{ http .authorizeHttpRequests(auth -> auth
	 * .requestMatchers("/public/**").permitAll()
	 * .requestMatchers("/actuator/**").permitAll() .anyRequest().authenticated() )
	 * .oauth2ResourceServer(oauth2 -> oauth2 .jwt(Customizer.withDefaults())//jwt
	 * -> jwt.jwkSetUri(jwkSetUri) ); return http.build(); }
	 * 
	 * @Bean JwtDecoder jwtDecoder() { return
	 * NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build(); }
	 */
	
}
