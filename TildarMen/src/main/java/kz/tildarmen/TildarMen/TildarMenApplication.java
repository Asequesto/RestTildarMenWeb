package kz.tildarmen.TildarMen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TildarMenApplication {

	public static void main(String[] args) {
		SpringApplication.run(TildarMenApplication.class, args);

		System.out.println("✅ TildarMen application is running!");
	}

}
