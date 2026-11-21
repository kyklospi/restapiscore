package restapiscore.rules.dim1;

import org.junit.jupiter.api.Test;
import restapiscore.rules.ElementCounter;
import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class R13PathLowercaseHyphenNoTrailSlashFileExtTest {
    private ElementCounter run(String fileName) throws Exception {
        URL resource = getClass().getResource("/rules/R1.3/" + fileName);
        assertNotNull(resource);
        Path file = Path.of(resource.toURI());
        SpecLoader.ParsedResult result = SpecLoader.load(file);
        return new R13PathLowercaseHyphenNoTrailSlashFileExt().evaluate(new ApiModel(result.openApi()));
    }

    @Test
    void passFileHasNoViolations() throws Exception {
        ElementCounter counter = run("pass.yaml");
        assertEquals(5, counter.checked());
        assertEquals(5, counter.compliant());
        assertTrue(counter.violations().isEmpty());
    }

    @Test
    void failFileHasFourViolations() throws Exception {
        ElementCounter counter = run("fail.yaml");
        assertEquals(4, counter.checked());
        assertEquals(0, counter.compliant());
        assertEquals(4, counter.violations().size());
        assertEquals("Path /Users/{id}: 'Users' has uppercase letters", counter.violations().get(0).message());
        assertEquals("Path /user_profiles: 'user_profiles' has an underscore", counter.violations().get(1).message());
        assertEquals("Path /files/report.json: 'report.json' has a file extension", counter.violations().get(2).message());
        assertEquals("Path /users/: ends with a slash", counter.violations().getLast().message());
    }

    @Test
    void emptyFileChecksNothing() throws Exception {
        assertEquals(0, run("empty.yaml").checked());
        assertTrue(run("empty.yaml").violations().isEmpty());
    }
}
