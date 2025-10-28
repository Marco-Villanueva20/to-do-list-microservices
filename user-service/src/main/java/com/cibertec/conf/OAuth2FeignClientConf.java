package com.cibertec.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import feign.RequestInterceptor;
import org.springframework.http.HttpHeaders;

@Configuration
public class OAuth2FeignClientConf {

	 /**
     * Configura el administrador de clientes autorizados de OAuth2.
     * Este se encarga de gestionar el token del cliente Feign usando Client Credentials.
     */
	/*@Bean
	OAuth2AuthorizedClientManager authorizedClientManager(
		ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
		
		  // Define el proveedor que usará el flujo "client_credentials"
		OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
		
		// Crea el manager que usa la base de datos (o memoria) para guardar los tokens
		AuthorizedClientServiceOAuth2AuthorizedClientManager manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
		
		manager.setAuthorizedClientProvider(provider);
		
		
		return manager;
	}
	
	/**
     * Interceptor Feign que añade el token de acceso OAuth2 en cada request.
     * Así Feign podrá comunicarse con otros microservicios protegidos.
    */
	/*
	@Bean
	RequestInterceptor feignOAuth2RequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
		return requestTamplate->{
			 // Construimos la solicitud OAuth2
			OAuth2AuthorizeRequest authorizedRequest = OAuth2AuthorizeRequest
					  .withClientRegistrationId("recomendation-s2s") // ID del client en tu configuración application.yml
					  .principal("feign-client") // nombre simbólico del cliente (no usuario)
					.build();
			
			// Autorizamos el cliente para obtener el token
			OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizedRequest);
			
			  // Validamos que haya token
			if(authorizedClient == null) {
				throw new IllegalStateException("No se pudo autorizar al cliente");
			}
			 // Extraemos el token
			String accessToken = authorizedClient.getAccessToken().getTokenValue();
			// Añadimos el token en la cabecera Authorization
			requestTamplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		};
	}	 */
}
