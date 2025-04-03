package sdk.enterprise.Constants;

public enum ConstantStrings {
    BUSINESS_CATEGORY_GAMING("GAMING"),
    CONTENT_TYPE("json");


    private final String message;

    ConstantStrings(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

