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
					.directory(System.getProperty("user.dir")) // Load from project root
					.ignoreIfMissing() // Don't fail if .env is missing (use default values)
					.load();

			// Set both system properties and environment variables
			// This ensures Spring Boot can read them via @Value("${...}")
			dotenv.entries().forEach(entry -> {
				String key = entry.getKey();
				String value = entry.getValue();
				System.setProperty(key, value);
				// Print loaded env variables for debugging (comment out in production)
				System.out.println("Loaded env variable: " + key + " = " +
					(key.contains("SECRET") || key.contains("PASSWORD") ? "****" : value));
			});

			System.out.println("✓ Successfully loaded .env file with " + dotenv.entries().size() + " variables");
		} catch (Exception e) {
			System.out.println("⚠ Warning: Could not load .env file: " + e.getMessage());
			System.out.println("Using default values from application.properties");
		}

		SpringApplication.run(FashionApplication.class, args);
	}

}
