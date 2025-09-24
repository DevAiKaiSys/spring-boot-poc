package com.example.websocketredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WebsocketredisApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketredisApplication.class, args);
	}

}
