package com.cibertec.conf;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials(true);
	        config.setAllowedOrigins(List.of("http://localhost:4200"));
	        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(List.of("*"));
	        config.setExposedHeaders(List.of("Authorization"));

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return source;
	    }

	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
	    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	    .csrf(ServerHttpSecurity.CsrfSpec::disable)
	    
	    .authorizeExchange(authorize -> 
	    authorize.anyExchange().authenticated()
	        )
	        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
	    //opcional mas confuso pero conciso .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt) lo mismo
	    return http.build();
	}
    
   
}