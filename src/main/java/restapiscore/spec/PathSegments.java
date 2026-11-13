package restapiscore.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for splitting a path into its segments.
 * Many rules look at single segments of a path.
 */
public class PathSegments {

    /**
     * Splits a path into its segments.
     *
     * @param path the path to split
     * @return a list of segments
     */
    public static List<String> split(String path) {
        List<String> result = new ArrayList<>();
        for (String part : path.split("/")) {
            if (!part.isEmpty()) {
                result.add(part);
            }
        }
        return result;
    }

    /**
     * Checks if a segment is a parameter.
     * e.g. from /users/{userId}, {userId} is a parameter, but 'users' is not.
     * @param segment the segment to check
     * @return true if the segment is a parameter, false otherwise
     */
    public static boolean isParameter(String segment) {
        return segment.startsWith("{") && segment.endsWith("}");
    }
}