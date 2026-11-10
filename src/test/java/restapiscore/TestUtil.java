package restapiscore;

import restapiscore.spec.ApiModel;
import restapiscore.spec.SpecLoader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public final class TestUtil {
    private TestUtil() {
    }

    public static Path resource(String relativePath) {
        URL url = TestUtil.class.getClassLoader().getResource(relativePath);
        if (url == null) {
            throw new IllegalArgumentException("Test file not found: " + relativePath);
        }
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    public static SpecLoader.ParsedResult load(String relativePath) {
        return new SpecLoader().load(resource(relativePath));
    }

    public static ApiModel model(String relativePath) {
        SpecLoader.ParsedResult result = load(relativePath);
        assertThat(result.openApi()).as("parse " + relativePath + ": " + result.messages()).isNotNull();
        return new ApiModel(result.openApi());
    }
}
