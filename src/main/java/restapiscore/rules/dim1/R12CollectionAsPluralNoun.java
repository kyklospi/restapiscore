package restapiscore.rules.dim1;

import restapiscore.config.Config;
import restapiscore.rules.*;
import restapiscore.scoring.RuleScorer;
import restapiscore.spec.ApiModel;
import restapiscore.spec.JsonPointers;
import restapiscore.spec.Location;
import restapiscore.spec.PathSegments;

import java.util.List;

/**
 * Rule R1.2: Collections use plural nouns.
 *
 * <p>Element: each literal path segment that is directly followed by a {@code {param}} segment, for example
 * {@code users} in {@code /users/{id}}. Compliant if the last word of the segment is plural.
 *
 * <p>Heuristic for English plurals: the last word ends with "s" or is in the irregular plural list, and it is
 * not in the list of singular words ending with "s".
 *
 * <p>Known limits: a collection without an item path ({@code /users} only) is not checked,
 * a single resource such as {@code /users/{id}/profile} is skipped,
 * plural forms that are not in the lists ({@code /cacti}) fail, and non-English resource names are not supported.
 */
public class R12CollectionAsPluralNoun implements Rule {
    private static final String RULE_ID = "R1.2";
    private static final RuleCard CARD = new RuleCard(
            RULE_ID, Dimension.D1, "Collections use plural nouns",
            "Massé 2011; RESTRuler",
            "Each collection segment (a literal segment followed by a path parameter)" +
                    " is compliant if it is a plural noun.",
            "Use plural nouns for collections, for example /users/{id} instead of /user/{id}.");

    private static final List<String> IRREGULAR_PLURALS = List.of("data", "metadata", "people", "children");
    private static final List<String> SINGULAR_ENDING_IN_S = List.of("status", "address", "access", "business", "process", "class", "progress");

    private boolean isPlural(String word) {
        if (IRREGULAR_PLURALS.contains(word)) return true;
        if (SINGULAR_ENDING_IN_S.contains(word)) return false;
        return word.endsWith("s");
    }

    @Override
    public RuleCard card() {
        return CARD;
    }

    @Override
    public RuleResult evaluate(ApiModel model, Config config) {
        ElementCounter counter = new ElementCounter();
        for (String path : model.paths()) {
            List<String> segments = PathSegments.split(path);

            for (int i = 0; i < segments.size() - 1; i++) {
                String segment = segments.get(i);
                String next = segments.get(i + 1);

                // Skip if the current segment is a parameter or the next segment is not a parameter
                // e.g., /users/{id} -> 'users' is checked, but /users/{id}/profile -> 'profile' is not checked
                if (PathSegments.isParameter(segment) || !PathSegments.isParameter(next)) {
                    continue;
                }
                if (isPlural(PathSegments.lastWord(segment))) {
                    counter.pass();
                } else {
                    counter.fail(new Violation(RULE_ID, new Location(JsonPointers.path(path)),
                            "Collection name '" + segment + "' is not a plural noun"));
                }
            }
        }
        Score score = RuleScorer.score(counter.checked(), counter.compliant(), config.thresholds());
        return new RuleResult(RULE_ID, score, counter.checked(), counter.compliant(), counter.violations(), null);
    }
}
