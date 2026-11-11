package restapiscore.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DimensionTest {

    @Test
    void thereAreNineDimensionsWithTitles() {
        assertEquals(9, Dimension.values().length);
        assertEquals("Resource modeling and URI design", Dimension.D1.title());
        assertEquals("HATEOAS", Dimension.D9.title());
    }
}