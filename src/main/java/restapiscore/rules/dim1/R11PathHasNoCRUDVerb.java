package restapiscore.rules.dim1;

import restapiscore.rules.*;
import restapiscore.spec.ApiModel;
import restapiscore.spec.JsonPointers;
import restapiscore.spec.Location;
import restapiscore.spec.PathSegments;

import java.util.List;

/**
 * Rule R1.1: Path has no CRUD verb.
 *
 * <p>Element: each path. Compliant if no literal segment starts with a CRUD verb from the configured verb list.
 *
 * <p>Heuristic: a segment "starts with a CRUD verb" when its first word (split at hyphens, underscores and camelCase
 * humps) equals a verb, so {@code getUsers} and {@code get-users} are violations while {@code settings} and
 * {@code addresses} are not. Known false positives: nouns that are also verbs ({@code /run}, {@code /set},
 * {@code /check} as in bank checks, {@code /list} as in a shopping list).
 */
public class R11PathHasNoCRUDVerb implements Rule {
    private static final String RULE_ID = "R1.1";
    private static final RuleCard CARD = new RuleCard(
            RULE_ID, Dimension.D1, "Path has no CRUD verb",
            "Massé 2011; RESTRuler",
            "Each path is compliant if no literal segment starts with a CRUD verb such as get, create or delete.",
            "Name resources with nouns and express the action through the HTTP method.");

    /**
     * List of verbs that are not allowed as the first word of a literal segment (Massé 2011), page 18).
     * @see <a href="https://archive.org/details/restapidesignrul0000mass">Massé 2011</a>
     * This list is not exhaustive, but covers the most common verbs used in REST APIs.
     * do and make are included because they are too vague as a controller resource, and should be replaced with a more specific verb.
     */
    private static final List<String> CRUD_VERBS = List.of("get", "retrieve", "fetch", "load", "find", "search",
            "post", "create", "save", "make", "put", "update", "add", "delete", "remove", "patch", "do");

    /**
     * Action verbs are allowed only as the last segment to identify a controller resource (Massé 2011, page 16-17).
     * controller resource models an action that does not fit the CRUD methods
     * e.g. /students/morgan/register is allowed, but /register/students/morgan is not.
     * @see <a href="https://archive.org/details/restapidesignrul0000mass">Massé 2011</a>
     */
    private static final List<String> ACTION_VERBS = List.of("register", "dedupe", "execute", "send", "receive",
            "approve", "reject", "activate", "deactivate", "enable", "disable", "start", "stop", "pause", "resume", "cancel",
            "confirm", "decline", "subscribe", "unsubscribe", "login", "logout", "authenticate", "authorize");

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
                String message = "Path '" + verbSegment + "' starts with a CRUD verb";
                counter.fail(new Violation(RULE_ID, location, message));
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
        List<String> segments = PathSegments.split(path);
        for (int i = 0; i < segments.size(); i++) {
            String segment = segments.get(i);
            if (PathSegments.isParameter(segment)) {
                continue;
            }
            String word = PathSegments.firstWord(segment);
            boolean isLast = (i == segments.size() - 1);
            if (CRUD_VERBS.contains(word)) {
                return segment;
            }
            if (ACTION_VERBS.contains(word) && !isLast) {
                return segment;
            }
        }
        return null;
    }
}
