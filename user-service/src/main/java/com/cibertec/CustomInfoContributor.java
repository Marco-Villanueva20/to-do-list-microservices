package com.cibertec;

import org.springframework.boot.actuate.info.Info.Builder; 
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {
	
	@Override
	public void contribute(Builder builder) {
		builder.withDetail("app", "Eureka Server");
		builder.withDetail("version", "1.0.0");
		builder.withDetail("description", "Servicio de descubrimiento de microservicios");
		builder.withDetail("author", "Marco Antonio");
	}
	
}
