package com.truecaller.enterprise.sdk.Utils;

import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.LoginRequest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;


public class TokenManager {
    private static volatile String token;

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
        String loginUrl = System.getenv(ConstantStrings.LOGIN_URL.getMessage());

        if (email != null && password != null && loginUrl != null) {
            LoginRequest loginRequest = new LoginRequest(email, password, ConstantStrings.PORTAL.getMessage());
            token = given().log().all().contentType(ContentType.JSON).body(loginRequest).when()
                    .post(loginUrl)
                    .then().log().all().assertThat().statusCode(HttpStatus.SC_OK).extract().path("token");
        }
        return token;
    }


}
