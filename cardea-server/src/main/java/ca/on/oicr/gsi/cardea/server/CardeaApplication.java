package ca.on.oicr.gsi.cardea.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CardeaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardeaApplication.class, args);
	}

}
