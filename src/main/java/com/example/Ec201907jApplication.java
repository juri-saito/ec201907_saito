package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Ec201907jApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Ec201907jApplication.class, args);
	}

	   @Override    
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		   return application.sources(Ec201907jApplication.class);
	   } 
	   
	   //WebAPI実行に使用
	   @Bean
	    public RestTemplate setRestTemplate(){
	        return new RestTemplate();
	    }
	   //作成されるRestTemplateをより詳細に制御できるらしい
//	   @Bean
//	   public RestTemplate restTemplate(RestTemplateBuilder builder) {
//	      // Do any additional configuration here
//	      return builder.build();
//	   }
	
}
