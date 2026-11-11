package restapiscore.rules;

/** Score of a rule, used in the reports and in the rule catalogue. */
public enum Score {
    ZERO(0),
    ONE(1),
    TWO(2),
    NA(-1);

    private final int points;

    Score(int points) {
        this.points = points;
    }

    public boolean isApplicable() {
        return this != NA;
    }

    public int points() {
        if (this == NA) {
            throw new IllegalStateException("Not applicable, it has no points");
        }
        return points;
    }

    /** "0", "1", "2" or "NA", used in the reports. */
    public String label() {
        return this == NA ? "NA" : String.valueOf(points);
    }
}
