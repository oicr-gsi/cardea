package ca.on.oicr.gsi.qcgateetlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QcGateEtlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcGateEtlApiApplication.class, args);
	}

}
