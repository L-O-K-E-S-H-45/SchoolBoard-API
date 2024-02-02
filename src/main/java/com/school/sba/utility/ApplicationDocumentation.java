package com.school.sba.utility;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class ApplicationDocumentation {
	
	Contact contact() {
		return new Contact()
				.name("Loki")
				.email("lokeshjamadar45@gmail.com")
				.url("mycontact.info.in");
	}
	
	Info info() {
		return new Info()
				.title("School-Board-API")
				.version("1.0v")
				.description("School-Board-API is a Restful API using Spring Boot and MYSQL database")
				.contact(contact());
	}
	/*
	 * @Bean is used to Map DTO Object to corresponding Entity Object and visa-versa
	 */
	
	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(info());
	}
	
	@Bean
	ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
