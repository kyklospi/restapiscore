package restapiscore.spec;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Loads an OpenAPI specification from a local file.
 *
 * <p>Swagger 2.0 documents are converted to OpenAPI 3.0 by the parser; OpenAPI 3.0 and 3.1 are read directly.
 * References are resolved (external files are pulled into {@code components}) but not inlined, so a
 * {@code $ref} to a component schema stays visible to the rules.
 */
public final class SpecLoader {

    /**
     * Result of a parsing attempt: either a parsed {@link OpenAPI} or a list of parser messages.
     *
     * @param openApi  the parsed document, or {@code null} when parsing failed
     * @param messages warnings and errors reported by the parser (never {@code null})
     */
    public record ParsedResult(OpenAPI openApi, List<String> messages) {

        public boolean isSuccess() {
            return openApi != null;
        }
    }

    public ParsedResult load(Path file) {
        Objects.requireNonNull(file, "file");
        if (!Files.exists(file)) {
            return new ParsedResult(null, List.of("File not found: " + file));
        }

        ParseOptions options = new ParseOptions();
        options.setResolve(true);       // load $ref targets, also from other files (shared)
        options.setResolveFully(false); // keep $ref, do not copy the schemas in place
        options.setFlatten(false);      // keep inline schemas as written

        SwaggerParseResult result;
        try {
            result = new OpenAPIParser().readLocation(file.toAbsolutePath().toString(), null, options);
        } catch (RuntimeException e) {
            return new ParsedResult(null, List.of("Parser failure: " + e.getMessage()));
        }
        if (result == null) {
            return new ParsedResult(null, List.of("Parser returned no result for " + file));
        }

        List<String> messages = new ArrayList<>();
        if (result.getMessages() != null) {
            messages.addAll(result.getMessages());
        }
        if (result.getOpenAPI() == null && messages.isEmpty()) {
            messages.add("The file is not an OpenAPI or Swagger description: " + file);
        }
        return new ParsedResult(result.getOpenAPI(), messages);
    }
}
