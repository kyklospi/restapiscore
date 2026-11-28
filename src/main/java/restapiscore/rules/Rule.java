package restapiscore.rules;

import restapiscore.config.Config;
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
     * @param config the configuration to use for evaluation
     * @return the result of the evaluation
     */
    RuleResult evaluate(ApiModel model, Config config);
}
