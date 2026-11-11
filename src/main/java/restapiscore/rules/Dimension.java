package restapiscore.rules;

/** Dimension of a rule, used in the reports and in the rule catalogue. */
public enum Dimension {
    D1("Resource modeling and URI design"),
    D2("HTTP methods and status codes"),
    D3("Error handling"),
    D4("Versioning"),
    D5("Documentation"),
    D6("Authentication and authorization"),
    D7("Pagination and filtering"),
    D8("Caching"),
    D9("HATEOAS");

    private final String title;

    Dimension(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
