package restapiscore.rules;

import restapiscore.spec.Location;

/**
 * One non-compliant element.
 *
 * @param ruleId   the ID of the rule that was violated
 * @param location where the element lives in the OpenAPI document
 * @param message  why the element is not compliant
 */
public record Violation(String ruleId, Location location, String message) { }
