package com.cibertec.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	  @Bean JwtDecoder jwtDecoder() { 
		NimbusJwtDecoder jwtDecoder =
		NimbusJwtDecoder.withJwkSetUri("http://localhost:9000/oauth2/jwks").build();

	    OAuth2TokenValidator<Jwt> withIssuer =
	        JwtValidators.createDefaultWithIssuer("http://localhost:9000");
	    OAuth2TokenValidator<Jwt> audienceValidator =
	        new JwtAudienceValidator("angular-app"); // acepta tokens para angular-app
	    OAuth2TokenValidator<Jwt> validator =
	        new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

	    jwtDecoder.setJwtValidator(validator);
	    return jwtDecoder;}
	  
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.GET, "/api/todo/**").hasAuthority("SCOPE_read")
				.requestMatchers(HttpMethod.GET, "/api/todo/**").hasAuthority("read")
				.anyRequest().authenticated())
		.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		return http.build();
	}

}

