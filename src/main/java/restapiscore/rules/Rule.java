package restapiscore.rules;

import restapiscore.spec.ApiModel;

/**
 * A rule that can be evaluated against an API model.
 */
public interface Rule {
    RuleCard card();

    ElementCounter evaluate(ApiModel model);
}
