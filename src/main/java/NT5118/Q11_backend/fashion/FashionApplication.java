package NT5118.Q11_backend.fashion;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		RabbitAutoConfiguration.class,
		RedisAutoConfiguration.class
})
public class FashionApplication {

	public static void main(String[] args) {
		// Load .env file and set environment variables
		try {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing() // Don't fail if .env is missing (use default values)
					.load();

			// Set environment variables from .env file
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
		} catch (Exception e) {
			System.out.println("Warning: Could not load .env file: " + e.getMessage());
		}

		SpringApplication.run(FashionApplication.class, args);
	}

}
