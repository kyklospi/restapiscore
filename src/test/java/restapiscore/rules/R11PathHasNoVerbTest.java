package restapiscore.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class R11PathHasNoVerbTest {
    private ElementCounter run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.1/" + fileName);
        Assertions.assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);
        return new R11PathHasNoVerb().evaluate(new ApiModel(result.openApi()));
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        ElementCounter counter = run("pass.yaml");
        assertEquals(4, counter.checked());
        assertEquals(4, counter.compliant());
        assertTrue(counter.violations().isEmpty());
    }

    @Test
    void failFileHasThreeViolations() throws Exception {
        ElementCounter counter = run("fail.yaml");
        assertEquals(4, counter.checked());
        assertEquals(1, counter.compliant());
        assertEquals(3, counter.violations().size());
        assertEquals("/paths/~1getUsers", counter.violations().getFirst().location().pointer());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        assertEquals(0, run("empty.yaml").checked());
    }
}
