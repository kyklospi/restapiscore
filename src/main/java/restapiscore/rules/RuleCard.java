package restapiscore.rules;

/** Data transfer object of a rule, used in reports and in the rule catalogue. */
public record RuleCard(String id, Dimension dimension, String name, String source,
                       String description, String fix) {
}