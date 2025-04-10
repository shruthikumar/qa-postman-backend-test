package sdk.enterprise.Constants;

public enum ConstantStrings {
    BUSINESS_CATEGORY_GAMING("GAMING"),
    CONTENT_TYPE("json"),
    APP_LOGO("appLogo"),
    CONSENT_REQUEST("consentRequest"),
    PROJECT_ID("X-Project-Id");





    private final String message;

    ConstantStrings(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

