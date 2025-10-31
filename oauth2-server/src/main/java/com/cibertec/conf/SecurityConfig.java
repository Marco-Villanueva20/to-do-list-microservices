package com.cibertec.conf;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	//Donde se aplica las configuraciones predeterminadas y se habilita el openid 
	@Bean
	@Order(1)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
	        throws Exception {
	    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
	            OAuth2AuthorizationServerConfigurer.authorizationServer();

	    http
	        .cors(Customizer.withDefaults())
	        .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
	        // IMPORTANTE: aplicar el configurer primero (o con 'with(...)' como antes)
	        .with(authorizationServerConfigurer, (authorizationServer) ->
	            authorizationServer.oidc(Customizer.withDefaults())
	        )

	        // Evitar CSRF para los endpoints del Authorization Server (token, authorize, jwks, etc.)
	        .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.getEndpointsMatcher()))

	        // Permisos: permitir discovery/jwks y las operaciones del token (POST) sin redirect a login
	        .authorizeHttpRequests((authorize) -> authorize
	            .requestMatchers("/.well-known/**", "/oauth2/jwks", "/.well-known/openid-configuration").permitAll()
	            // Permitir explícitamente token/revoke/introspect para que no provoquen redirect a /login
	            .requestMatchers(org.springframework.http.HttpMethod.POST, "/oauth2/token", "/oauth2/revoke", "/oauth2/introspect").permitAll()
	            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
	            .anyRequest().authenticated()
	        )

	        // Redirigir a login SOLO si la petición es de tipo text/html (tu config anterior ya hacía esto)
	        .exceptionHandling((exceptions) -> exceptions
	            .defaultAuthenticationEntryPointFor(
	                new LoginUrlAuthenticationEntryPoint("/login"),
	                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
	            )
	        );

	    return http.build();
	}

	
	//Donde se maneja el login con formulario
	@Bean
	@Order(2)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.configurationSource(corsConfigurationSource())) 
		.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().authenticated()
		)
			// Form login handles the redirect to the login page from the
			// authorization server filter chain
		.formLogin(Customizer.withDefaults());

		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	

	
	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		 JdbcRegisteredClientRepository repo = new JdbcRegisteredClientRepository(jdbcTemplate);
		 if (repo.findByClientId("angular-app") == null) {
			 RegisteredClient angularClient = RegisteredClient.withId(UUID.randomUUID().toString())
					    .clientId("angular-app")
					    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
					    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
					    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
					    .redirectUri("http://localhost:4200/")
					    .redirectUri("http://localhost:4200/index.html")
					    .redirectUri("http://127.0.0.1:4200/")
					    .postLogoutRedirectUri("http://localhost:4200/")
					    .scope(OidcScopes.OPENID)
					    .scope(OidcScopes.PROFILE)
					    .scope("read")
					    .scope("write")
					    .clientSettings(ClientSettings.builder()
					    		.requireProofKey(true)
					    		.requireAuthorizationConsent(false).build())
					    .tokenSettings(TokenSettings.builder()
					            .accessTokenTimeToLive(Duration.ofMinutes(5))
					            .refreshTokenTimeToLive(Duration.ofDays(14))
					            .reuseRefreshTokens(true) // o false según tu necesidad
					            .build())
					    .build();
			 repo.save(angularClient);
		 }
		

		 return repo;
	}

	//Genera las llaves publica y privada para firmar los tokens
	@Bean
	JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	//Genera las llaves publica y privada
	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	//Decodifica el token JWT
	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	//Configuracion del servidor de autorizacion
	@Bean
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
		.build();
	}
	

}
