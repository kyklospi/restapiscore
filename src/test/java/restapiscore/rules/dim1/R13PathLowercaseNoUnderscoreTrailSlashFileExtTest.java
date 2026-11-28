package restapiscore.rules.dim1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapiscore.config.Config;
import restapiscore.rules.*;
import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class R13PathLowercaseNoUnderscoreTrailSlashFileExtTest {
    private RuleResult run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.3/" + fileName);
        assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);

        URL configUrl = getClass().getResource("/config/test.yaml");
        Assertions.assertNotNull(configUrl);
        Config config = Config.loadFromYaml(Path.of(configUrl.toURI()));
        return new R13PathLowercaseNoUnderscoreTrailSlashFileExt().evaluate(new ApiModel(result.openApi()), config);
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        RuleResult result = run("pass.yaml");
        assertEquals(5, result.checked());
        assertEquals(5, result.compliant());
        assertTrue(result.violations().isEmpty());
        assertEquals(Score.TWO, result.score());
    }

    @Test
    void failFileHasFourViolations() throws Exception {
        RuleResult result = run("fail.yaml");
        assertEquals(4, result.checked());
        assertEquals(0, result.compliant());
        assertEquals(4, result.violations().size());

        assertEquals("Path /Users/{id}: 'Users' has uppercase letters", result.violations().get(0).message());
        assertEquals("Path /user_profiles: 'user_profiles' has an underscore", result.violations().get(1).message());
        assertEquals("Path /files/report.json: 'report.json' has a file extension", result.violations().get(2).message());
        assertEquals("Path /users/: ends with a slash", result.violations().getLast().message());

        assertEquals(Score.ZERO, result.score());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        RuleResult result = run("empty.yaml");
        assertEquals(0, result.checked());
        assertTrue(result.violations().isEmpty());
        assertEquals(Score.NA, result.score());
    }
}
