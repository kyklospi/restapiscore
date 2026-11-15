package restapiscore.rules;

import restapiscore.spec.ApiModel;

/**
 * A rule that can be evaluated against an API model.
 */
public interface Rule {

    /**
     * Returns the metadata of this rule.
     */
    RuleCard card();

    /**
     * Evaluates this rule against the given API model.
     *
     * @param model the API model to evaluate
     * @return the result of the evaluation
     */
    ElementCounter evaluate(ApiModel model);
}
