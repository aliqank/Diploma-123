package com.autoparts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AutoPartsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoPartsApplication.class, args);
	}

}
