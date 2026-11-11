package restapiscore.rules;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void applicableScoresHavePoints() {
        assertEquals(0, Score.ZERO.points());
        assertEquals(1, Score.ONE.points());
        assertEquals(2, Score.TWO.points());
        assertTrue(Score.TWO.isApplicable());
    }

    @Test
    void naIsNotApplicableAndHasNoPoints() {
        assertFalse(Score.NA.isApplicable());
        assertThatException().isThrownBy(Score.NA::points)
                .isInstanceOf(IllegalStateException.class)
                .withMessageContaining("Not applicable");
    }

    @Test
    void labelsAreUsedInReports() {
        assertEquals("1", Score.ONE.label());
        assertEquals("NA", Score.NA.label());
    }
}