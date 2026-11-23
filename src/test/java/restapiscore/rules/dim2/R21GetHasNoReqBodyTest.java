package restapiscore.rules.dim2;

import org.junit.jupiter.api.Test;
import restapiscore.rules.ElementCounter;
import restapiscore.rules.Violation;
import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class R21GetHasNoReqBodyTest {

    private ElementCounter run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R2.1/" + fileName);
        assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);
        return new R21GetHasNoReqBody().evaluate(new ApiModel(result.openApi()));
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        ElementCounter counter = run("pass.yaml");
        assertEquals(2, counter.checked());
        assertEquals(2, counter.compliant());
        assertTrue(counter.violations().isEmpty());
    }

    @Test
    void failFileHasOneViolation() throws Exception {
        ElementCounter counter = run("fail.yaml");
        assertEquals(2, counter.checked());
        assertEquals(1, counter.compliant());
        assertEquals(1, counter.violations().size());

        Violation v = counter.violations().getFirst();
        assertEquals("R2.1", v.ruleId());
        assertEquals("GET operation '/users/{id}' has a request body", v.message());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        assertEquals(0, run("empty.yaml").checked());
        assertTrue(run("empty.yaml").violations().isEmpty());
    }
}
