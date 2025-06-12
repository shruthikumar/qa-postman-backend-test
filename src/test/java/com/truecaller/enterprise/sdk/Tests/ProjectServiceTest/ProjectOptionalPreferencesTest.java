package com.truecaller.enterprise.sdk.Tests.ProjectServiceTest;

import com.truecaller.enterprise.sdk.Base.BaseTest;
import com.truecaller.enterprise.sdk.Client.RestClient;
import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.CredentialsRequest;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.ProjectRequest;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.OtpVerificationStatusResponse;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.ProjectResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.truecaller.enterprise.sdk.CustomAnnotations.TestCaseId;
import com.truecaller.enterprise.sdk.Utils.StringUtils;

import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ProjectOptionalPreferencesTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        restClient = new RestClient(prop, baseURI);
    }

    @Test(description = "Business should be able to create credentials and update optional preferences in TEST mode")
    @TestCaseId("SDK_TS_11")
    public void businessShouldBeAbleToCreateCredentialsAndUpdateOptionalPreferencesInTestMode() {
        // Step 1: Create a new project
        ProjectRequest projectRequest = new ProjectRequest(
                StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = projectResponse.getId();
        Map<String, String> headers = restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId);

        // Step 2: Add initial Android credentials
        CredentialsRequest.Fingerprint fingerprint = CredentialsRequest.Fingerprint.builder()
                .fingerPrint(StringUtils.getRandomFingerprint())
                .label(StringUtils.getRandomLabel())
                .build();

        CredentialsRequest.Metadata metadata = CredentialsRequest.Metadata.builder()
                .packageName(StringUtils.getPackageName())
                .fingerPrints(Arrays.asList(fingerprint))
                .build();

        CredentialsRequest credentialsRequest = CredentialsRequest.builder()
                .platform(ConstantStrings.PLATFORM.getMessage())
                .metadata(metadata)
                .build();

        restClient.post(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, headers, credentialsRequest)
                .then().statusCode(HttpStatus.SC_OK);;

        // Step 3: Enable optional preferences
        OtpVerificationStatusResponse otpVerificationStatusResponse = restClient.put(PROJECT_SERVICE_OTP_VERIFICATION_ACTIVATE_ENDPOINT_V1, headers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(OtpVerificationStatusResponse.class);

        assertEquals(otpVerificationStatusResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());

        // Step 4 : Disable optional preferences
        OtpVerificationStatusResponse deactivateotpVerificationStatusResponse = restClient.put(PROJECT_SERVICE_OTP_VERIFICATION_DEACTIVATE_V1, headers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(OtpVerificationStatusResponse.class);

        assertEquals(deactivateotpVerificationStatusResponse.getStatus(), ConstantStrings.STATUS_INACTIVE.getMessage());
    }
}