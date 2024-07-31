package org.example.rest_back;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestBackApplication {
	static {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("spring.datasource.url", "jdbc:mysql://" + dotenv.get("RDS_HOSTNAME") + ":" + dotenv.get("RDS_PORT") + "/" + dotenv.get("RDS_DB_NAME"));
		System.setProperty("spring.datasource.username", dotenv.get("RDS_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("RDS_PASSWORD"));

		System.setProperty("AWS_ACCESS_KEY", dotenv.get("AWS_ACCESS_KEY"));
		System.setProperty("AWS_SECRET_KEY", dotenv.get("AWS_SECRET_KEY"));

		System.setProperty("JWT_SECRET_KEY", dotenv.get(("JWT_SECRET_KEY")));
		System.setProperty("MAIL_PASSWORD", dotenv.get(("MAIL_PASSWORD")));
	}
	public static void main(String[] args) {
		SpringApplication.run(RestBackApplication.class, args);
	}

}
