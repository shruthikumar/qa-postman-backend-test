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
    TEST_MODE("test"),
    REVIEW_MODE("ready_to_review"),
    PRODUCTION_MODE("production"),
    STATUS_ACTIVE("active"),
    LOGIN_URL("LOGIN_URL"),
    STATUS_INACTIVE("inactive"),
    BUSINESS_CATEGORY_REALTY("REALTY");

    private final String message;

    ConstantStrings(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}


