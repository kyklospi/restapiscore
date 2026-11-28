package restapiscore.rules;

import java.util.List;

/**
 * Result of a rule check.
 *
 * @param ruleId     the ID of the rule that was checked
 * @param score      the score of the rule check
 * @param checked    number of elements checked by the rule
 * @param compliant  number of elements that are compliant with the rule
 * @param violations list of non-compliant elements
 * @param note       optional note about the rule check
 */
public record RuleResult(String ruleId, Score score, int checked, int compliant,
                         List<Violation> violations, String note) {

    public RuleResult {
        violations = violations == null ? List.of() : List.copyOf(violations);

        if (score.equals(Score.NA)) {
            note = "Nothing was checked";
        } else if (note == null || note.isBlank()) {
            note = compliant + " of " + checked + " elements are compliant.";
        }
    }
}