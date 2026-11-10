package restapiscore.spec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import restapiscore.TestUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class SpecLoaderTest {

    @TempDir
    Path tempDir;

    private final SpecLoader loader = new SpecLoader();

    @Test
    void missingFileFails() {
        SpecLoader.ParsedResult result = loader.load(Path.of("does/not/exist.yaml"));
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).singleElement().asString().contains("File not found");
    }

    @Test
    void directoryFails() {
        SpecLoader.ParsedResult result = loader.load(tempDir);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).isNotEmpty();
    }

    @Test
    void invalidFileFailsWithMessages() {
        SpecLoader.ParsedResult result = loader.load(TestUtil.resource("spec/invalid.yaml"));
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).isNotEmpty();
    }

    @Test
    void validFileHasTitleAndMessagesList() {
        SpecLoader.ParsedResult result = loader.load(TestUtil.resource("spec/no-paths.yaml"));
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.openApi().getInfo().getTitle()).isEqualTo("No Paths");
        assertThat(result.messages()).isNotNull();
    }

    @Test
    void externalRefIsLoadedIntoComponents() {
        SpecLoader.ParsedResult result = loader.load(TestUtil.resource("spec/external-ref.yaml"));
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.openApi().getComponents().getSchemas()).containsKey("Error");
    }

    @Test
    void notAnOpenApiFileFails() throws IOException {
        Path file = tempDir.resolve("other.yaml");
        Files.writeString(file, "name: test\n");
        SpecLoader.ParsedResult result = loader.load(file);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).isNotEmpty();
    }
}