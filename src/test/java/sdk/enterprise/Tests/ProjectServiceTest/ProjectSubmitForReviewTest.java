package sdk.enterprise.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Constants.Constants;
import sdk.enterprise.Constants.ErrorCodes;
import sdk.enterprise.Constants.ErrorConstants;
import sdk.enterprise.CustomAnnotations.TestCaseId;
import sdk.enterprise.DataProvider.ExcelDataProvider;
import sdk.enterprise.Entities.ErrorEntities.ErrorResponse;
import sdk.enterprise.Entities.RequestEntities.*;
import sdk.enterprise.Entities.ResponseEntities.*;
import sdk.enterprise.Utils.JsonPathValidator;
import sdk.enterprise.Utils.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static sdk.enterprise.Utils.StringUtils.getFileFromResources;

public class ProjectSubmitForReviewTest extends BaseTest {

    @BeforeMethod
    public void setup() {
        restClient = new RestClient(prop, baseURI);
    }

    @Test(description = "Business Should Able to Create the Project And Submit For Review",
            dataProvider = "accountData",
            dataProviderClass = ExcelDataProvider.class)
    @TestCaseId("SDK_TS_1")
    public void businessShouldAbleToSubmitTheProjectForReview(String expectedPartnerId,
                                                              String expectedEmail,
                                                              String expectedCountry,
                                                              String expectedContactFirstName,
                                                              String expectedContactLastName,
                                                              String expectedLegalBusinessName) {

        // Step 1: Get Partner Account Details
        PartnerAccountResponse partnerAccountResponse = restClient.get(PARTNER_SERVICE_ENDPOINT_V1)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(PartnerAccountResponse.class);

        assertEquals(partnerAccountResponse.getPartnerId(), expectedPartnerId);
        assertEquals(partnerAccountResponse.getEmail(), expectedEmail);
        assertEquals(partnerAccountResponse.getCountry(), expectedCountry);
        assertEquals(partnerAccountResponse.getContactFirstName(), expectedContactFirstName);
        assertEquals(partnerAccountResponse.getContactLastName(), expectedContactLastName);
        assertEquals(partnerAccountResponse.getLegalBusinessName(), expectedLegalBusinessName);

        // Step 2: Create Project
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage()
        );

        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);

        String projectId = projectResponse.getId();
        String projectName = projectRequest.getName();

        // Step 3: Add Android Credentials
        Map<String, String> headers = restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId);

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

        CredentialsResponse credentialsResponse = restClient.post(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, headers, credentialsRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        String clientId = credentialsResponse.getClientId();

        // Step 4: Get Credentials
        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, clientId);

        CredentialsResponse getCredentialsResponse = restClient.get(serviceUrl, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        assertEquals(getCredentialsResponse.getClientId(), credentialsResponse.getClientId());

        // Step 5: Get Project Details by project id
        DetailsOfAllProjectsResponse projectDetailsResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(projectDetailsResponse.getId(), projectId);
        assertEquals(projectDetailsResponse.getName(), projectName);

        // Step 6: Add consent details
        File appLogoFile = getFileFromResources(Constants.APP_LOGO_FILE_NAME);

        ConsentRequest consentRequest = ConsentRequest.builder()
                .appName(StringUtils.getCompanyName())
                .scopes(Constants.SCOPES)
                .userSupportEmail(StringUtils.getEmail())
                .developerEmail(StringUtils.getEmail())
                .developerName(StringUtils.getFullName())
                .privacyPolicyUrl(StringUtils.getPrivacyPolicyUrl())
                .tosUrl(StringUtils.getTosUrl())
                .homePageUrl(StringUtils.getHomePageUrl())
                .mandatoryScopes(Constants.MANDATORY_SCOPES)
                .build();

        String consentJson = JsonPathValidator.convertObjectToJsonString(consentRequest);

        ConsentResponse consentResponse = restClient.postMultiPart(PROJECT_SERVICE_CONSENT_ENDPOINT_V1, headers, appLogoFile, consentJson)
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().as(ConsentResponse.class);
        assertEquals(consentResponse.getSummary(), Constants.CONSENT_SCREEN_SAVED);

        // Step 7: Fetch consent details
        ConsentResponse getConsentResponse = restClient.get(PROJECT_SERVICE_CONSENT_ENDPOINT_V2, headers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ConsentResponse.class);

        assertEquals(getConsentResponse.getAppName(), consentRequest.getAppName());
        assertEquals(getConsentResponse.getUserSupportEmail(), consentRequest.getUserSupportEmail());
        assertEquals(getConsentResponse.getDeveloperEmail(), consentRequest.getDeveloperEmail());
        assertEquals(getConsentResponse.getDeveloperName(), consentRequest.getDeveloperName());
        assertEquals(getConsentResponse.getPrivacyPolicyUrl(), consentRequest.getPrivacyPolicyUrl());
        assertEquals(getConsentResponse.getTosUrl(), consentRequest.getTosUrl());
        assertEquals(getConsentResponse.getHomePageUrl(), consentRequest.getHomePageUrl());

        // Step 8: Add mobile number
        TestPhoneNumberRequest testPhoneNumberRequestBody = TestPhoneNumberRequest.builder()
                .phoneNumber(StringUtils.getRandomMobileNumber())
                .build();

        String requestBody = JsonPathValidator.convertObjectToJsonString(testPhoneNumberRequestBody);

        restClient.post(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1, headers, requestBody)
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        // Step 9: Get list of phone numbers added to the project
        String responseBody = restClient.get(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().asString();

        assertTrue(responseBody.contains(String.valueOf(testPhoneNumberRequestBody.getPhoneNumber())));

        // Step 10 : Add Optional Preferences / Enable optional preferences
        OtpVerificationStatusResponse otpVerificationStatusResponse = restClient.put(PROJECT_SERVICE_OTP_VERIFICATION_ACTIVATE_ENDPOINT_V1, headers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(OtpVerificationStatusResponse.class);

        assertEquals(otpVerificationStatusResponse.getClientId(), clientId);
        assertEquals(otpVerificationStatusResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());

        // Step 11: Verification Prepare
        VerificationPrepareResponse verificationPrepareResponse = restClient.post(PROJECT_SERVICE_VERIFICATION_PREPARE_ENDPOINT_V2, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(VerificationPrepareResponse.class);

        assertEquals(verificationPrepareResponse.getId(), projectId);
        assertEquals(verificationPrepareResponse.getAppName(), consentRequest.getAppName());

        // Step 12 : Submit for review
        OptionalPreferencesRequest optionalPreferencesRequestBody = OptionalPreferencesRequest.builder()
                .partnerComments(StringUtils.getRandomParagraph())
                .build();

        restClient.post(PROJECT_SERVICE_VERIFICATION_SUBMIT_ENDPOINT_V2, headers, optionalPreferencesRequestBody)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(description = "Business should not be able to submit a project for review without developer details and other mandatory information")
    @TestCaseId("SDK_TS_2")
    public void shouldThrowErrorForMissingMandatoryFieldsInCreateProject() {

        // Step 1: Create a new project with only minimal details (no dev info or credentials)
        ProjectRequest projectRequest = new ProjectRequest(
                StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());

        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract()
                .as(ProjectResponse.class);

        String projectId = projectResponse.getId();

        // Step 2: Attempt to prepare project for verification review
        Map<String, String> headers = restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId);

        ErrorResponse errorResponse = restClient.post(PROJECT_SERVICE_VERIFICATION_PREPARE_ENDPOINT_V2, headers)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(ErrorResponse.class);

        assertEquals(errorResponse.getMessage(), ErrorConstants.CONSENT_SCREEN_DETAILS_NOT_FOUND_MSG);
        assertEquals(errorResponse.getStatus(), ErrorCodes.CONSENT_SCREEN_DETAILS_NOT_FOUND_ERROR_CODE);
    }
}