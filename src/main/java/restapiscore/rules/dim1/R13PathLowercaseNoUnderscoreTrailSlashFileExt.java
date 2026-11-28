package restapiscore.rules.dim1;

import restapiscore.config.Config;
import restapiscore.rules.*;
import restapiscore.scoring.RuleScorer;
import restapiscore.spec.ApiModel;
import restapiscore.spec.JsonPointers;
import restapiscore.spec.Location;
import restapiscore.spec.PathSegments;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule R1.3: Path uses lowercase letters, no underscore, no file extension, no trailing slash.
 *
 * <p>Element: each path. Compliant if every path segment has lowercase letters, no underscore as a word separator,
 * and no file extension such as {@code .json}. The path must not have a trailing slash {@code /} (the root path {@code /} is allowed).
 * A dot alone does not count as extension, so microsoft.sql is compliant and report.json is not.
 *
 * <p>Known limits: RESTRuler uses a dictionary of 126k words for hyphens and a list of more than 800 extensions.
 * restapiscore is a prototype, a segment such as userprofiles (two words without a hyphen) and an uncommon extension are not found.
 * @see <a href="https://arxiv.org/pdf/2402.13710">RESTRuler 2024</a>
 */
public class R13PathLowercaseNoUnderscoreTrailSlashFileExt implements Rule {
    private static final String RULE_ID = "R1.3";
    private static final RuleCard CARD = new RuleCard(
            RULE_ID, Dimension.D1, "Path uses lowercase letters, no underscore, no file extension, no trailing slash",
            "Massé 2011; RESTRuler",
            "Each path is compliant if every literal segment has lowercase letters, no underscore, no file extension and no trailing slash.",
            "Use lowercase letters and hyphens instead of underscores in paths, avoid file extensions, and do not end paths with a trailing slash.");

    private static final List<String> EXTENSIONS = List.of("json", "xml", "html", "htm", "csv", "txt",
            "pdf", "yaml", "yml", "png", "jpg", "jpeg", "gif", "svg", "zip");

    @Override
    public RuleCard card() {
        return CARD;
    }

    @Override
    public RuleResult evaluate(ApiModel model, Config config) {
        ElementCounter counter = new ElementCounter();
        for (String path : model.paths()) {
            List<String> problems = new ArrayList<>();
            if (path.length() > 1 && path.endsWith("/")) {
                problems.add("ends with a slash");
            }
            List<String> segments = PathSegments.split(path);
            for (String segment : segments) {
                if (PathSegments.isParameter(segment)) continue;
                if (!segment.equals(segment.toLowerCase())) problems.add("'" + segment + "' has uppercase letters");
                if (segment.contains("_")) problems.add("'" + segment + "' has an underscore");
                if (hasFileExtension(segment)) problems.add("'" + segment + "' has a file extension");
            }
            if (problems.isEmpty()) {
                counter.pass();
            } else {
                counter.fail(new Violation(RULE_ID, new Location(JsonPointers.path(path)),
                        "Path " + path + ": " + String.join(", ", problems)));
            }
        }
        Score score = RuleScorer.score(counter.checked(), counter.compliant(), config.thresholds());
        return new RuleResult(RULE_ID, score, counter.checked(), counter.compliant(), counter.violations(), null);
    }

    private boolean hasFileExtension(String segment) {
        int dot = segment.lastIndexOf('.');
        if (dot < 0) return false;
        return EXTENSIONS.contains(segment.substring(dot + 1).toLowerCase());
    }
}
