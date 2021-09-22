package br.com.sankhya.commercial.analisegiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class AnaliseGiroApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)  {
		return application.sources(getClass());
	}

	public static void main(String[] args) {
		SpringApplication.run(AnaliseGiroApplication.class, args);
	}

}
