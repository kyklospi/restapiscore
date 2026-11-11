package restapiscore.spec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import restapiscore.TestUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SpecLoaderTest {

    @TempDir
    Path tempDir;

    private final SpecLoader loader = new SpecLoader();

    @Test
    void missingFileFails() {
        SpecLoader.ParsedResult result = loader.load(Path.of("does/not/exist.yaml"));
        assertFalse(result.isSuccess());
        assertThat(result.messages()).singleElement().asString().contains("File not found");
    }

    @Test
    void directoryFails() {
        SpecLoader.ParsedResult result = loader.load(tempDir);
        assertFalse(result.isSuccess());
        assertFalse(result.messages().isEmpty());
    }

    @Test
    void invalidFileFailsWithMessages() {
        SpecLoader.ParsedResult result = loader.load(TestUtil.resource("spec/invalid.yaml"));
        assertFalse(result.isSuccess());
        assertFalse(result.messages().isEmpty());
    }

    @Test
    void validFileHasTitleAndMessagesList() {
        SpecLoader.ParsedResult result = loader.load(TestUtil.resource("spec/no-paths.yaml"));
        assertTrue(result.isSuccess());
        assertEquals("No Paths", result.openApi().getInfo().getTitle());
        assertNotNull(result.messages());
    }

    @Test
    void externalRefIsLoadedIntoComponents() {
        SpecLoader.ParsedResult result = loader.load(TestUtil.resource("spec/external-ref.yaml"));
        assertTrue(result.isSuccess());
        assertTrue(result.openApi().getComponents().getSchemas().containsKey("Error"));
    }

    @Test
    void notAnOpenApiFileFails() throws IOException {
        Path file = tempDir.resolve("other.yaml");
        Files.writeString(file, "name: test\n");
        SpecLoader.ParsedResult result = loader.load(file);
        assertFalse(result.isSuccess());
        assertFalse(result.messages().isEmpty());
    }
}