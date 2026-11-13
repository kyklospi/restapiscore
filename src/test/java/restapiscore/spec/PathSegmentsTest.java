package restapiscore.spec;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathSegmentsTest {

    @Test
    void splitsPathIntoSegments() {
        List<String> segments = PathSegments.split("/users/{id}/orders");
        assertThat(segments).containsExactly("users", "{id}", "orders");
    }

    @Test
    void rootPathHasNoSegments() {
        assertThat(PathSegments.split("/")).isEmpty();
    }

    @Test
    void recognizesPathParameters() {
        assertTrue(PathSegments.isParameter("{id}"));
        assertFalse(PathSegments.isParameter("users"));
    }
}