package com.healthtrack.backend.Cofiguration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfiguration {
	
	@Bean
	public OpenAPI  openAPICustomizer() {
		  return new OpenAPI()
				  .info(new Info()
						    .title("HealthTrack API")
						    .description("HealthTrack related API's with Spring Security + JWT")
						    .version("v1.0")
						)
				  .components(new Components().addSecuritySchemes("bearerAuth", 
						  new SecurityScheme()
						  .type(SecurityScheme.Type.HTTP)
						  .scheme("bearer")
						  .bearerFormat("JWT")))
				  .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
				           
	}
	@Bean 
	public OpenApiCustomizer apiCustomizer() {
		return Openapi->{
			Openapi.getPaths().forEach((path,pathItem)->{
				pathItem.readOperations().forEach((operation)->{
					if(!path.startsWith("/api/auth")) {
						operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
					}
				});
			});
		};
	}

}
