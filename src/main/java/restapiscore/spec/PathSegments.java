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

    /**
     * Returns the literal segments of a path, i.e. the segments that are not parameters.
     * e.g. from /users/{userId}/orders, the literal segments are 'users' and 'orders'.
     * @param path the path to split
     * @return a list of literal segments
     */
    public static List<String> literalSegments(String path) {
        List<String> result = new ArrayList<>();
        for (String segment : split(path)) {
            if (!isParameter(segment)) {
                result.add(segment);
            }
        }
        return result;
    }

    /**
     * Returns the first word of a segment, which is the part before a dash, underscore, or uppercase letter.
     * e.g. from 'getUsers', the first word is 'get'; from 'user-id', the first word is 'user'.
     * @param segment the segment to analyze
     * @return the first word of the segment
     */
    public static String firstWord(String segment) {
        for (int i = 0; i < segment.length(); i++) {
            char c = segment.charAt(i);
            if (c == '-' || c == '_' || (i > 0 && Character.isUpperCase(c))) {
                return segment.substring(0, i).toLowerCase();
            }
        }
        return segment.toLowerCase();
    }

    /**
     * Returns the last word of a segment, which is the part after the last dash, underscore, or uppercase letter.
     * e.g. from 'getUsers', the last word is 'users'; from 'user-id', the last word is 'id'.
     * @param segment the segment to analyze
     * @return the last word of the segment
     */
    public static String lastWord(String segment) {
        String word = segment;
        int cut = Math.max(word.lastIndexOf('-'), word.lastIndexOf('_'));
        if (cut >= 0) {
            word = word.substring(cut + 1);
        }
        for (int i = word.length() - 1; i > 0; i--) {
            if (Character.isUpperCase(word.charAt(i))) {
                word = word.substring(i);
                break;
            }
        }
        return word.toLowerCase();
    }
}