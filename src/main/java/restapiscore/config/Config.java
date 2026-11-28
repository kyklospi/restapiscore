package restapiscore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Configuration for the scoring of REST APIs.
 */
public record Config(Thresholds thresholds) {

    /**
     * Thresholds on the share {@code p} of compliant elements: {@code p >= high} scores 2, {@code p >= low}
     * scores 1, otherwise 0.
     */
    public record Thresholds(double high, double low) {
        public Thresholds {
            if(high < 0 || low < 0 || high > 1 || low > 1) {
                throw new IllegalArgumentException("Thresholds must be between 0 and 1");
            }
            if(high < low) {
                throw new IllegalArgumentException("High threshold must be greater than or equal to low threshold");
            }
        }
    }

    public static Config defaults() {
        Thresholds t = new Thresholds(0.9, 0.5);
        return new Config(t);
    }

    public static Config loadFromYaml(Path yamlFilePath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Thresholds thresholds;
        try {
            String text = java.nio.file.Files.readString(yamlFilePath);
            thresholds = text.isBlank() ? defaults().thresholds() : mapper.readValue(text, Config.class).thresholds();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config file " + yamlFilePath + ": " + e.getMessage(), e);
        }
        return new Config(thresholds);
    }
}
