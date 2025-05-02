package sdk.enterprise.Base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Configuration.ConfigurationManager;

import java.util.Properties;

public class BaseTest {
    //Service URLs:
    public static final String PROJECT_SERVICE_ENDPOINT = "/v1/project";
    public static final String PARTNER_SERVICE_ENDPOINT = "/v2/partner";
    public static final String PARTNER_SERVICE_ENDPOINT_V1 = "/v1/partner/account";
    public static final String PROJECT_SERVICE_PROJECT_DETAILS_ENDPOINT_V1 = "/v1/project/details";
    public static final String V2_CREDENTIALS = "/v2/credentials";
    public static final String PROJECT_SERVICE_CONSENT_V1 = "/v1/project/consent";
    public static final String PROJECT_SERVICE_CONSENT_V2=   "/v2/project/consent";
    public static final String PROJECT_TEST_PHONE_NUMBER = "/v1/project/testphonenumber";
    public static final String V1_OTP_VERIFICATION_ACTIVATE =  "v1/otp/verification/activate";
    public static final String V2_VERIFICATION_PREPARE = "/v2/verification/prepare";
    public static final String V2_VERIFICATION_SUBMIT =  "/v2/verification/submit";


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
