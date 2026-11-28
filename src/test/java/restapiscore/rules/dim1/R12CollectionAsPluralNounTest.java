package restapiscore.rules.dim1;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class R12CollectionAsPluralNounTest {
    private RuleResult run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.2/" + fileName);
        Assertions.assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);

        URL configUrl = getClass().getResource("/config/test.yaml");
        Assertions.assertNotNull(configUrl);
        Config config = Config.loadFromYaml(Path.of(configUrl.toURI()));
        return new R12CollectionAsPluralNoun().evaluate(new ApiModel(result.openApi()), config);
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        RuleResult result = run("pass.yaml");
        assertEquals(4, result.checked());
        assertEquals(4, result.compliant());
        assertTrue(result.violations().isEmpty());
        assertEquals(Score.TWO, result.score());
    }

    @Test
    void failFileHasTwoViolations() throws Exception {
        RuleResult result = run("fail.yaml");
        assertEquals(3, result.checked());
        assertEquals(1, result.compliant());
        assertEquals(2, result.violations().size());
        assertEquals("/paths/~1user~1{id}", result.violations().getFirst().location().pointer());
        assertEquals("/paths/~1users~1{id}~1address~1{addressId}", result.violations().getLast().location().pointer());

        Violation v = result.violations().getFirst();
        assertEquals("R1.2", v.ruleId());
        assertEquals("Collection name 'user' is not a plural noun", v.message());

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
