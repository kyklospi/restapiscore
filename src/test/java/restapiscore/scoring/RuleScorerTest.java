package restapiscore.scoring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapiscore.config.Config;
import restapiscore.rules.Score;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RuleScorerTest {

    @Test
    void shareIsCompliantOverChecked() {
        assertEquals(0.75, RuleScorer.share(4, 3));
        assertEquals(0.0, RuleScorer.share(3, 0));
    }

    @Test
    void shareCheckedNullThrowsException() {
        assertEquals("checked must be positive",
                assertThrows(IllegalArgumentException.class, () -> RuleScorer.share(0, 0)).getMessage()
        );
    }

    @Test
    void ruleScoreDefaultThresholds() throws Exception {
        URL configurl = getClass().getResource("/config/empty.yaml");
        Assertions.assertNotNull(configurl);
        Config config = Config.loadFromYaml(Path.of(configurl.toURI()));

        assertEquals(Score.TWO, RuleScorer.score(1, 1, config.thresholds()));
        assertEquals(Score.TWO, RuleScorer.score(10, 9, config.thresholds()));
        assertEquals(Score.ONE, RuleScorer.score(10, 8, config.thresholds()));
        assertEquals(Score.ONE, RuleScorer.score(10, 5, config.thresholds()));
        assertEquals(Score.ZERO, RuleScorer.score(10, 4, config.thresholds()));
        assertEquals(Score.ZERO, RuleScorer.score(10, 0, config.thresholds()));
    }

    @Test
    void ruleScoreNotApplicable() {
        Config.Thresholds t = new Config.Thresholds(0.9, 0.5);
        assertEquals(Score.NA, RuleScorer.score(0, 0, t));
    }

    @Test
    void ruleScoreConfigThresholds() throws Exception {
        URL configurl = getClass().getResource("/config/test.yaml");
        Assertions.assertNotNull(configurl);
        Config config = Config.loadFromYaml(Path.of(configurl.toURI()));

        assertEquals(Score.TWO, RuleScorer.score(10, 8, config.thresholds()));
        assertEquals(Score.ONE, RuleScorer.score(10, 5, config.thresholds()));
        assertEquals(Score.ZERO, RuleScorer.score(10, 3, config.thresholds()));
    }
}