package rahma.backend.gestionPDEK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Pfe2025Application {
	public static void main(String[] args) {
		SpringApplication.run(Pfe2025Application.class, args);
	}
}