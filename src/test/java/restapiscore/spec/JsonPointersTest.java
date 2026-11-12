package restapiscore.spec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPointersTest {
    @Test
    void escapesSlashAndTilde() {
        assertEquals("~1users~1{id}", JsonPointers.escape("/users/{id}"));
        assertEquals("a~0b", JsonPointers.escape("a~b"));
    }

    @Test
    void buildsOperationPointer() {
        assertEquals("/paths/~1users~1{id}/get", JsonPointers.operation("/users/{id}", "GET"));
    }
}
