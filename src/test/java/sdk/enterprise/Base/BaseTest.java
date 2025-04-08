package sdk.enterprise.Base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Configuration.ConfigurationManager;

import java.util.Properties;

public class BaseTest {
    //Service URLs:
    public static final String PROJECT_SERVICE_ENDPOINT = "/v1/project";
    public static final String PARTNER_SERVICE_ENDPOINT = "/v2/partner";


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
