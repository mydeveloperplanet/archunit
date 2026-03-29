package com.mydeveloperplanet.archunit.taikai.springconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.mydeveloperplanet.archunit.taikai.springconfig.config")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
