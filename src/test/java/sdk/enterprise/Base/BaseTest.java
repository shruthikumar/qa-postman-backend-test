package sdk.enterprise.Base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.*;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Configuration.ConfigurationManager;

import java.util.Properties;

public class BaseTest {
    //Service URLs:
    public static final String PROJECT_SERVICE_ENDPOINT_V1 = "/v1/project";
    public static final String PARTNER_SERVICE_ENDPOINT_V2 = "/v2/partner";
    public static final String PARTNER_SERVICE_ENDPOINT_V1 = "/v1/partner/account";
    public static final String PROJECT_SERVICE_DETAILS_ENDPOINT_V1 = "/v1/project/details";
    public static final String PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2 = "/v2/credentials";
    public static final String PROJECT_SERVICE_CONSENT_ENDPOINT_V1 = "/v1/project/consent";
    public static final String PROJECT_SERVICE_CONSENT_ENDPOINT_V2 =   "/v2/project/consent";
    public static final String PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1 = "/v1/project/testphonenumber";
    public static final String PROJECT_SERVICE_OTP_VERIFICATION_ACTIVATE_ENDPOINT_V1 =  "v1/otp/verification/activate";
    public static final String PROJECT_SERVICE_VERIFICATION_PREPARE_ENDPOINT_V2 = "/v2/verification/prepare";
    public static final String PROJECT_SERVICE_VERIFICATION_SUBMIT_ENDPOINT_V2 =  "/v2/verification/submit";
    public static final String PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1 = "/v1/project/deactivate";
    public static final String PROJECT_SERVICE_ACTIVATE_ENDPOINT_V1 = "/v1/project/activate";

    protected ConfigurationManager config;
    protected Properties prop;
    protected RestClient restClient;
    protected String baseURI;

    @Parameters({"baseURI"})
    @BeforeTest
    public void setUp(String baseURI) {
        RestAssured.filters(new AllureRestAssured());
        config = new ConfigurationManager();
        prop = config.initProp();
        this.baseURI = baseURI;
    }

    @AfterTest
    public void tearDown() {
        //Reset RestAssured if not done per method
        RestAssured.reset();
    }
}
