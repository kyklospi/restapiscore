package restapiscore.rules.dim1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapiscore.rules.ElementCounter;
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

    private ElementCounter run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.1/" + fileName);
        Assertions.assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);
        return new R11PathHasNoCRUDVerb().evaluate(new ApiModel(result.openApi()));
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        ElementCounter counter = run("pass.yaml");
        assertEquals(5, counter.checked());
        assertEquals(5, counter.compliant());
        assertTrue(counter.violations().isEmpty());
    }

    @Test
    void failFileHasThreeViolations() throws Exception {
        ElementCounter counter = run("fail.yaml");
        assertEquals(4, counter.checked());
        assertEquals(1, counter.compliant());
        assertEquals(3, counter.violations().size());
        assertEquals("/paths/~1getUsers", counter.violations().getFirst().location().pointer());
        assertEquals("/paths/~1users~1create", counter.violations().get(1).location().pointer());
        assertEquals("/paths/~1register~1users~1{id}", counter.violations().getLast().location().pointer());

        Violation v = counter.violations().getFirst();
        assertEquals("R1.1", v.ruleId());
        assertEquals("Path 'getUsers' starts with a CRUD verb", v.message());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        assertEquals(0, run("empty.yaml").checked());
        assertTrue(run("empty.yaml").violations().isEmpty());
    }

    @Test
    void actionVerbIsAllowedOnlyAsLastSegment() throws Exception {
        ElementCounter counter = run("fail.yaml");
        List<String> pointers = counter.violations().stream()
                .map(v -> v.location().pointer()).toList();

        assertThat(pointers).contains("/paths/~1register~1users~1{id}");
    }
}
