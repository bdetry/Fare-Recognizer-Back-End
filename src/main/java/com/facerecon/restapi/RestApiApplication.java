package com.facerecon.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.facerecon.restapi.model.Stock;

@ServletComponentScan
@SpringBootApplication
public class RestApiApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RestApiApplication.class);
	}
	
	@Bean
	public Stock stock() {
		return new Stock();
	}
	

	


}

