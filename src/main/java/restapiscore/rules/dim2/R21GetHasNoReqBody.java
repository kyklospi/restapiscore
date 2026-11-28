package restapiscore.rules.dim2;

import io.swagger.v3.oas.models.PathItem;
import restapiscore.config.Config;
import restapiscore.rules.*;
import restapiscore.scoring.RuleScorer;
import restapiscore.spec.ApiModel;
import restapiscore.spec.OperationRef;

/**
 * R2.1 GET has no request body.
 *
 * <p>Element: each GET operation. Compliant if no {@code requestBody} is declared.
 *
 * <p>Known limits: a request body that is not declared in the OpenAPI specification cannot be detected.
 */
public class R21GetHasNoReqBody implements Rule {
    private static final String RULE_ID = "R2.1";
    private static final RuleCard CARD = new RuleCard(
            RULE_ID, Dimension.D2, "GET has no request body",
            "Massé 2011; RESTRuler",
            "Each GET operation is compliant if it has no request body.",
            "Pass input to GET through path or query parameters. Use POST for complex queries.");

    @Override
    public RuleCard card() {
        return CARD;
    }


    @Override
    public RuleResult evaluate(ApiModel model, Config config) {
        ElementCounter counter = new ElementCounter();
        for (OperationRef op : model.operations(PathItem.HttpMethod.GET)) {
            if (op.operation().getRequestBody() == null) {
                counter.pass();
            } else {
                String message = "GET operation '" + op.path() + "' has a request body";
                counter.fail(new Violation(RULE_ID, op.location(), message));
            }
        }
        Score score = RuleScorer.score(counter.checked(), counter.compliant(), config.thresholds());
        return new RuleResult(RULE_ID, score, counter.checked(), counter.compliant(), counter.violations(), null);
    }
}
