package restapiscore.rules;

import restapiscore.spec.ApiModel;
import restapiscore.spec.JsonPointers;
import restapiscore.spec.Location;
import restapiscore.spec.PathSegments;

import java.util.List;

/**
 * Rule R1.1: Path has no verb.
 * Each path is compliant if no literal segment starts with a verb such as get, create or delete.
 * Name resources only with nouns and express the action through the HTTP method.
 */
public class R11PathHasNoVerb implements Rule {
    private static final RuleCard CARD = new RuleCard(
            "R1.1", Dimension.D1, "Path has no verb",
            "Massé 2011; RESTRuler",
            "Each path is compliant if no literal segment starts with a verb such as get, create or delete.",
            "Name resources with nouns and express the action through the HTTP method.");

    private static final List<String> VERBS = List.of(
            "get", "create", "update", "delete", "add", "remove", "fetch", "set", "list", "make", "do",
            "save", "check", "run", "execute", "send", "receive", "post", "put", "patch", "find", "search", "retrieve", "load");

    @Override
    public RuleCard card() {
        return CARD;
    }

    @Override
    public ElementCounter evaluate(ApiModel model) {
        ElementCounter counter = new ElementCounter();
        for (String path : model.paths()) {
            String verbSegment = findVerbSegment(path);
            if (verbSegment == null) {
                counter.pass();
            } else {
                Location location = new Location(JsonPointers.path(path));
                String message = "Segment '" + verbSegment + "' starts with a verb";
                counter.fail(new Violation("R1.1", location, message));
            }
        }
        return counter;
    }

    /**
     * Finds the first literal segment of the path that starts with a verb.
     * @param path the path to check
     * @return the first literal segment that starts with a verb, or null if none found
     */
    private String findVerbSegment(String path) {
        for (String segment : PathSegments.literalSegments(path)) {
            if (VERBS.contains(PathSegments.firstWord(segment))) {
                return segment;
            }
        }
        return null;
    }
}
