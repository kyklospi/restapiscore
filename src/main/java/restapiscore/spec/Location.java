package restapiscore.spec;

/**
 * Location of an element in an OpenAPI document.
 *
 * @param pointer JSON pointer to the element
 */
public record Location(String pointer) { }
