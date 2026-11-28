package restapiscore.scoring;

import restapiscore.config.Config;
import restapiscore.rules.Score;

/**
 * Turns the share {@code p} of compliant elements into a rule score:
 * {@code p >= high} gives 2, {@code low <= p < high} gives 1, {@code p < low} gives 0.
 * If rule checked is <= 0, the score is NA. Nothing is checked, so the rule is not counted.
 * Otherwise, share = passed / checked.
 * Example: share >= 0.9 gives 2, share >= 0.5 gives 1, and anything lower gives 0.
 */
public class RuleScorer {

    /**
     * Calculates the rule score based on the share of compliant elements and the provided thresholds.
     * If rule checked is <= 0, the score is NA. Nothing is checked, so the rule is not counted.
     *
     * @param checked   the number of elements checked by the rule
     * @param compliant the number of elements that are compliant with the rule
     * @param thresholds the thresholds for scoring
     * @return the score based on the share and thresholds
     */
    public static Score score(int checked, int compliant, Config.Thresholds thresholds) {
        if (checked <= 0) {
            return Score.NA;
        }

        double p = share(checked, compliant);

        if (p >= thresholds.high()) {
            return Score.TWO;
        }
        if (p >= thresholds.low()) {
            return Score.ONE;
        }
        return Score.ZERO;
    }

    /**
     * Calculates the share of compliant elements.
     *
     * @param checked   the number of elements checked by the rule
     * @param compliant the number of elements that are compliant with the rule
     * @return the share of compliant elements
     * @throws IllegalArgumentException if {@code checked} is not positive
     */
    public static double share(int checked, int compliant) {
        if (checked <= 0) {
            throw new IllegalArgumentException("checked must be positive");
        }
        return (double) compliant / checked;
    }
}
