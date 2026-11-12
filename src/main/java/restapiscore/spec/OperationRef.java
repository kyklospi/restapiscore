package restapiscore.spec;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

/**
 * One operation of the API together with its method, path and JSON pointer.
 *
 * @param method    the HTTP method
 * @param path      the path template, for example {@code /users/{id}}
 * @param pathItem  the enclosing path item (for path-level parameters)
 * @param operation the operation object
 */
public record OperationRef(PathItem.HttpMethod method, String path, PathItem pathItem, Operation operation) {

    /** Human-readable form such as {@code GET /users/{id}}, used as element description in violations. */
    public String describe() {
        return method.name() + " " + path;
    }

    /** JSON pointer to the operation, for example {@code #/paths/~1users~1{id}/get}. */
    public String pointer() {
        return JsonPointers.operation(path, method.name());
    }

    /** Location of the operation in the OpenAPI document. */
    public Location location() {
        return new Location(pointer());
    }
}
