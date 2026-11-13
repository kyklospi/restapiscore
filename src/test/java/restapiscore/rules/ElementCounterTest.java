package restapiscore.rules;

import org.junit.jupiter.api.Test;
import restapiscore.spec.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementCounterTest {
    @Test
    void countsPassesAndFailures() {
        ElementCounter ElementCounter = new ElementCounter();
        ElementCounter.pass();
        ElementCounter.pass();
        ElementCounter.fail(new Violation("R1.1", new Location("/paths/~1getUsers/get"), "Verb in path"));

        assertEquals(3, ElementCounter.checked());
        assertEquals(2, ElementCounter.compliant());
        assertEquals(1, ElementCounter.violations().size());
    }

    @Test
    void emptyElementCounterChecksNothing() {
        ElementCounter ElementCounter = new ElementCounter();

        assertEquals(0, ElementCounter.checked());
        assertEquals(0, ElementCounter.violations().size());
    }
}
