package com.project.starbucksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StarbucksApiApplication {

	public static void main(String[] args){
		new SpringApplication(StarbucksApiApplication.class,
							  ModuleConfiguration.class,
							  SwaggerConfiguration.class
		).run(args);
	}
}
