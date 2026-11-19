package restapiscore.spec;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void literalSegmentsSkipParameters() {
        assertThat(PathSegments.literalSegments("/users/{id}/orders"))
                .containsExactly("users", "orders");
    }

    @Test
    void firstWordIsCutAtHyphenUnderscoreOrCapital() {
        assertEquals("get", PathSegments.firstWord("getUsers"));
        assertEquals("user", PathSegments.firstWord("user-id"));
        assertEquals("create", PathSegments.firstWord("create_user"));
        assertEquals("users", PathSegments.firstWord("users"));
    }

    @Test
    void lastWordIsTheWholeSegmentIfNoSeparators() {
        assertEquals("users", PathSegments.lastWord("users"));
    }

    @Test
    void lastWordIsAfterLastHyphenUnderscoreOrCapital() {
        assertEquals("users", PathSegments.lastWord("getUsers"));
        assertEquals("id", PathSegments.lastWord("user-id"));
        assertEquals("user", PathSegments.lastWord("create_user"));
    }
}