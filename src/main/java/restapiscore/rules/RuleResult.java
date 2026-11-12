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
 */
public record RuleResult(String ruleId, Score score, int checked, int compliant,
                         List<Violation> violations) { }