package br.com.sankhya.commercial.analisegiro.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class Swagger {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				 .components(new Components()
						 
	                        .addSecuritySchemes("Basic", new SecurityScheme()
	    							.type(SecurityScheme.Type.HTTP)
	    							.scheme("basic")
	    					)
	                        
	                        .addSecuritySchemes("Bearer",
	       		                 new SecurityScheme().type(SecurityScheme.Type.HTTP)
	       		                 .scheme("bearer").bearerFormat("JWT")
	       		                .in(SecurityScheme.In.HEADER).name("Authorization"))
	                        
	                        .addParameters("Version", new Parameter()
	                                .in("header")
	                                .name("Version")
	                                .schema(new StringSchema())
	                                .required(false)))
				 
				 			.security(Arrays.asList(
	                        new SecurityRequirement().addList("Basic"),
	                        new SecurityRequirement().addList("Bearer")))
				 			
	               .info(new Info()
				.title("Análise de Giro")
				 .version("1.0a")
				 .description(" POC")
				 
				 .termsOfService("https://github.com/leonardopatrick/meuprojeto")
				 .license(new License().name("Sankhya").url("https://github.com/leonardopatrick/meuprojeto")));
	}
}