package restapiscore.rules;

import org.junit.jupiter.api.Test;
import restapiscore.spec.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleResultTest {

    @Test
    void keepsTheValuesItWasCreatedWith() {
        Violation violation = new Violation(
                "R1.1",
                new Location("/paths/~1getUsers/get"),
                "The path contains a verb"
        );

        RuleResult result = new RuleResult(
                "R1.1", Score.ONE, 10, 7, List.of(violation), "Path has no CRUD verb"
        );

        assertEquals("R1.1", result.ruleId());
        assertEquals(Score.ONE, result.score());
        assertEquals(10, result.checked());
        assertEquals(7, result.compliant());
        assertEquals(List.of(violation), result.violations());
        assertEquals("Path has no CRUD verb", result.note());
    }

    @Test
    void noteNullOrBlankDefaults() {
        Violation violation = new Violation(
                "R1.1",
                new Location("/paths/~1getUsers/get"),
                "The path contains a verb"
        );

        RuleResult resultNoteNull = new RuleResult(
                "R1.1", Score.ONE, 10, 7, List.of(violation), null
        );

        RuleResult resultNoteBlank = new RuleResult(
                "R1.1", Score.ONE, 10, 7, List.of(violation), ""
        );

        assertEquals("7 of 10 elements are compliant.", resultNoteNull.note());
        assertEquals("7 of 10 elements are compliant.", resultNoteBlank.note());
    }

    @Test
    void noteForNotApplicable() {
        RuleResult result = new RuleResult(
                "R1.1", Score.NA, 0, 0, List.of(), null
        );

        assertEquals("Nothing was checked", result.note());
    }
}
