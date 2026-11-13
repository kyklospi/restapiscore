package restapiscore.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for counting the number of elements checked and compliant, and collecting violations.
 * Elements are things like paths, operations, parameters, etc. that are checked by rules.
 */
public class ElementCounter {
    private int checked = 0;
    private int compliant = 0;
    private final List<Violation> violations = new ArrayList<>();

    /**
     * Marks an element as checked and compliant.
     */
    public void pass() {
        checked++;
        compliant++;
    }

    /**
     * Marks an element as checked but non-compliant.
     * @param violation the violation to add
     */
    public void fail(Violation violation) {
        checked++;
        violations.add(violation);
    }

    /**
     * Returns the number of elements checked.
     * @return the number of elements checked
     */
    public int checked() {
        return checked;
    }

    /**
     * Returns the number of elements compliant.
     * @return the number of elements compliant
     */
    public int compliant() {
        return compliant;
    }

    /**
     * Returns the list of violations.
     * @return the list of violations
     */
    public List<Violation> violations() {
        return violations;
    }
}
