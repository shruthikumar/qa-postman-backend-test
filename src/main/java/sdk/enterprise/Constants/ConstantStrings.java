package sdk.enterprise.Constants;

public enum ConstantStrings {
    BUSINESS_CATEGORY_GAMING("GAMING"),
    CONTENT_TYPE("json"),
    APP_LOGO("appLogo"),
    CONSENT_REQUEST("consentRequest"),
    PROJECT_ID("X-Project-Id"),
    EMAIL("EMAIL"),
    PASSWORD("PASSWORD"),
    PORTAL("developers_portal"),
    PLATFORM("Android"),
    MODE("test"),
    STATUS_ACTIVE("active"),
    CONSENT_SCREEN_SAVED("Consent screen saved");


    private final String message;

    ConstantStrings(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

