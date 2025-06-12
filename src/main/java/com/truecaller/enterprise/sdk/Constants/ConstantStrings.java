package com.truecaller.enterprise.sdk.Constants;

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

    public enum BusinessCategory {
        BFSI,
        EDTECH,
        GAMING,
        REALTY,
        FINTECH,
        GROCERY,
        AGRITECH,
        B2B_TOOLS,
        PARENTING,
        ECOMMERCE,
        HEALTHCARE,
        HOME_DECOR,
        JOB_PORTALS,
        NBU_COMMERCE,
        NEWS_MEDIA,
        URBAN_COMMUTE,
        FANTASY_SPORTS,
        FASHION_COMMERCE,
        COUPONS_REWARDS,
        FOOD_BEVERAGES,
        STREAMING_SERVICES,
        TRAVEL_TICKETING,
        UTILITY_SUPER_APPS,
        CLASSIFIEDS_VENDORS,
        HYPERLOCAL_ONDEMAND,
        AUTO_MARKETPLACES_OEM,
        MOVIES_EVENT_LISTINGS,
        SOCIAL_CONTENT_PLATFORMS,
        LOGISTICS;
    }
}