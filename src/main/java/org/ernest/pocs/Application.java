package org.ernest.pocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends ApplicationStarter{

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
