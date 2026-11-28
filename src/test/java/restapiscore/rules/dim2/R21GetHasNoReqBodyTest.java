package restapiscore.rules.dim2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapiscore.config.Config;
import restapiscore.rules.RuleResult;
import restapiscore.rules.Score;
import restapiscore.rules.Violation;
import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class R21GetHasNoReqBodyTest {

    private RuleResult run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R2.1/" + fileName);
        assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);

        URL configUrl = getClass().getResource("/config/test.yaml");
        Assertions.assertNotNull(configUrl);
        Config config = Config.loadFromYaml(Path.of(configUrl.toURI()));

        return new R21GetHasNoReqBody().evaluate(new ApiModel(result.openApi()), config);
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        RuleResult result = run("pass.yaml");
        assertEquals(2, result.checked());
        assertEquals(2, result.compliant());
        assertTrue(result.violations().isEmpty());
        assertEquals(Score.TWO, result.score());
    }

    @Test
    void failFileHasOneViolation() throws Exception {
        RuleResult result = run("fail.yaml");
        assertEquals(2, result.checked());
        assertEquals(1, result.compliant());
        assertEquals(1, result.violations().size());

        Violation v = result.violations().getFirst();
        assertEquals("R2.1", v.ruleId());
        assertEquals("GET operation '/users/{id}' has a request body", v.message());

        assertEquals(Score.ONE, result.score());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        RuleResult result = run("empty.yaml");
        assertEquals(0, result.checked());
        assertTrue(result.violations().isEmpty());
        assertEquals(Score.NA, result.score());
    }
}
