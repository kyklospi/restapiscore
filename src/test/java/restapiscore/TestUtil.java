package restapiscore;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

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
}
