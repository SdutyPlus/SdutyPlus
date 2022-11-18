package com.d205.sdutyplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SdutyplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdutyplusApplication.class, args);
	}

}
