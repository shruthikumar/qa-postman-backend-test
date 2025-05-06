package sdk.enterprise.Utils;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Entities.RequestEntities.LoginRequest;

import static io.restassured.RestAssured.given;


public class TokenManager {
    private static volatile String token;
    private static final String LOGIN_URI = "https://partner-account-noneu.truecaller.com/v1/account/login";


    public static String getToken() {
        if (token == null) {
            synchronized (TokenManager.class) {
                if (token == null) {
                    token = fetchBearerToken();
                }
            }
        }
        return token;
    }

    protected static String fetchBearerToken() {
        String email = System.getenv(ConstantStrings.EMAIL.getMessage());
        String password = System.getenv(ConstantStrings.PASSWORD.getMessage());

        if (email != null && password != null) {
            LoginRequest loginRequest = new LoginRequest(email, password, ConstantStrings.PORTAL.getMessage());
            token = given().log().all().contentType(ContentType.JSON).body(loginRequest).when()
                    .post(LOGIN_URI)
                    .then().log().all().assertThat().statusCode(HttpStatus.SC_OK).extract().path("token");
        }
        return token;
    }


}
