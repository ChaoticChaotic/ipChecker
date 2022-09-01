package com.chaoticchaotic.ipchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class IpCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpCheckerApplication.class, args);
	}
}
