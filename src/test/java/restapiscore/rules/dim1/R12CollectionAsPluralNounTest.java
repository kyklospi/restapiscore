package restapiscore.rules.dim1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapiscore.rules.ElementCounter;
import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class R12CollectionAsPluralNounTest {
    private ElementCounter run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.2/" + fileName);
        Assertions.assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);
        return new R12CollectionAsPluralNoun().evaluate(new ApiModel(result.openApi()));
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        ElementCounter counter = run("pass.yaml");
        assertEquals(4, counter.checked());
        assertEquals(4, counter.compliant());
        assertTrue(counter.violations().isEmpty());
    }

    @Test
    void failFileHasTwoViolations() throws Exception {
        ElementCounter counter = run("fail.yaml");
        assertEquals(3, counter.checked());
        assertEquals(1, counter.compliant());
        assertEquals(2, counter.violations().size());
        assertEquals("/paths/~1user~1{id}", counter.violations().getFirst().location().pointer());
        assertEquals("/paths/~1users~1{id}~1address~1{addressId}", counter.violations().getLast().location().pointer());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        assertEquals(0, run("empty.yaml").checked());
        assertTrue(run("empty.yaml").violations().isEmpty());
    }
}
