package Amadeu.ScraperVagas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScraperVagasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScraperVagasApplication.class, args);
	}

}
