package restapiscore.spec;

/** Utility class for creating JSON pointers.
 * A JSON pointer is an address separated by "/" for an element inside a JSON or YAML OpenAPI document.
 * The pointer /paths/~1users~1{id}/get means: go into paths, then into /users/{id}, then into get.
 * A path like /users/{id} contains / itself.
 * Without escaping, the pointer would look like this: #/paths//users/{id}/get, which is invalid.
 * The "/" in the path must be escaped to "~1", so the pointer becomes #/paths/~1users~1{id}/get.
 * The "~" character must also be escaped to "~0", so a path like /a~b/{id} becomes #/paths/~1a~0b~1{id}/get.
 * See RFC 6901 for details: <a href="https://datatracker.ietf.org/doc/html/rfc6901">...</a>
 * */
public class JsonPointers {

    /** Escapes a string for use in JSON pointers.
     * @param segment the string to escape
     * @return the escaped string
     */
    public static String escape(String segment) {
        return segment.replace("~", "~0").replace("/", "~1");
    }

    /** Creates a JSON pointer for an operation.
     * @param path the path of the operation
     * @param method the HTTP method of the operation
     * @return the JSON pointer
     */
    public static String operation(String path, String method) {
        return "/paths/" + escape(path) + "/" + method.toLowerCase();
    }
}