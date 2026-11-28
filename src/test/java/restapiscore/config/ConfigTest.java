package restapiscore.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void thresholds() {
        Config.Thresholds thresholds = new Config.Thresholds(0.9, 0.5);
        assertEquals(0.9, thresholds.high());
        assertEquals(0.5, thresholds.low());
    }

    @Test
    void thresholdsInvalid() {
        assertEquals("Thresholds must be between 0 and 1",
                assertThrows(IllegalArgumentException.class, () -> new Config.Thresholds(-0.1, 0.5)).getMessage()
        );
        assertEquals("Thresholds must be between 0 and 1",
                assertThrows(IllegalArgumentException.class, () -> new Config.Thresholds(0.8, -0.1)).getMessage()
        );
        assertEquals("Thresholds must be between 0 and 1",
                assertThrows(IllegalArgumentException.class, () -> new Config.Thresholds(1.1, 0.5)).getMessage()
        );
        assertEquals("Thresholds must be between 0 and 1",
                assertThrows(IllegalArgumentException.class, () -> new Config.Thresholds(0.8, 1.1)).getMessage()
        );
        assertEquals("High threshold must be greater than or equal to low threshold",
                assertThrows(IllegalArgumentException.class, () -> new Config.Thresholds(0.4, 0.5)).getMessage()
        );
    }

    @Test
    void thresholdsFromYamlFile() throws Exception {
        URL configurl = getClass().getResource("/config/test.yaml");
        Assertions.assertNotNull(configurl);
        Config config = Config.loadFromYaml(Path.of(configurl.toURI()));

        assertEquals(0.8, config.thresholds().high());
        assertEquals(0.4, config.thresholds().low());
    }

    @Test
    void thresholdsDefaultFromYamlFileEmpty() throws Exception {
        URL configurl = getClass().getResource("/config/empty.yaml");
        Assertions.assertNotNull(configurl);
        Config config = Config.loadFromYaml(Path.of(configurl.toURI()));

        assertEquals(0.9, config.thresholds().high());
        assertEquals(0.5, config.thresholds().low());
    }
}