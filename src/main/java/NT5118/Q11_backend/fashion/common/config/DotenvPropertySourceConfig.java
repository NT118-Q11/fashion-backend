package NT5118.Q11_backend.fashion.common.config; // điều chỉnh package theo project

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
public class DotenvPropertySourceConfig {

    private final ConfigurableEnvironment environment;

    public DotenvPropertySourceConfig(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void loadDotenv() {
        Path dotenvPath = Paths.get(".env");
        if (!Files.exists(dotenvPath)) {
            return; // no .env present
        }

        Map<String, Object> map = new HashMap<>();
        try (Stream<String> lines = Files.lines(dotenvPath)) {
            lines.map(String::trim)
                 .filter(l -> !l.isEmpty() && !l.startsWith("#"))
                 .forEach(l -> {
                     int idx = l.indexOf('=');
                     if (idx <= 0) return;
                     String key = l.substring(0, idx).trim();
                     String value = l.substring(idx + 1).trim();
                     // remove surrounding quotes if present
                     if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                         value = value.substring(1, value.length() - 1);
                     }
                     map.put(key, value);
                 });
        } catch (IOException ignored) {
            // ignore reading errors
        }

        if (!map.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", map));
        }
    }
}
