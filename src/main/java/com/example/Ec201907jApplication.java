package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Ec201907jApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Ec201907jApplication.class, args);
	}

	   @Override    
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		   return application.sources(Ec201907jApplication.class);
	   } 
	
}
