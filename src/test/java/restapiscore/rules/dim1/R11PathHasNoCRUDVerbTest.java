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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class R11PathHasNoCRUDVerbTest {

    private RuleResult run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.1/" + fileName);
        Assertions.assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);

        URL configurl = getClass().getResource("/config/test.yaml");
        Assertions.assertNotNull(configurl);
        Config config = Config.loadFromYaml(Path.of(configurl.toURI()));

        return new R11PathHasNoCRUDVerb().evaluate(new ApiModel(result.openApi()), config);
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
    void failFileHasThreeViolations() throws Exception {
        RuleResult result = run("fail.yaml");
        assertEquals(4, result.checked());
        assertEquals(1, result.compliant());
        assertEquals(3, result.violations().size());
        assertEquals("/paths/~1getUsers", result.violations().getFirst().location().pointer());
        assertEquals("/paths/~1users~1create", result.violations().get(1).location().pointer());
        assertEquals("/paths/~1register~1users~1{id}", result.violations().getLast().location().pointer());

        Violation v = result.violations().getFirst();
        assertEquals("R1.1", v.ruleId());
        assertEquals("Path 'getUsers' starts with a CRUD verb", v.message());

        assertEquals(Score.ZERO, result.score());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        RuleResult result = run("empty.yaml");
        assertEquals(0, result.checked());
        assertTrue(result.violations().isEmpty());
        assertEquals(Score.NA, result.score());
    }

    @Test
    void actionVerbIsAllowedOnlyAsLastSegment() throws Exception {
        RuleResult result = run("fail.yaml");
        List<String> pointers = result.violations().stream()
                .map(v -> v.location().pointer()).toList();

        assertThat(pointers).contains("/paths/~1register~1users~1{id}");
    }
}
