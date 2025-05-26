package sdk.enterprise.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Constants.Constants;
import sdk.enterprise.CustomAnnotations.TestCaseId;
import sdk.enterprise.Entities.RequestEntities.CredentialsRequest;
import sdk.enterprise.Entities.RequestEntities.FingerPrintRequest;
import sdk.enterprise.Entities.RequestEntities.ProjectRequest;
import sdk.enterprise.Entities.RequestEntities.UpdateCredentialRequest;
import sdk.enterprise.Entities.ResponseEntities.CredentialsResponse;
import sdk.enterprise.Entities.ResponseEntities.ProjectResponse;
import sdk.enterprise.Utils.StringUtils;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;


public class ProjectCredentialsTest extends BaseTest {
    @BeforeMethod
    public void setUp() {
        restClient = new RestClient(prop, baseURI);
    }

    @Test(description = "Business should be able to create credentials and update package name in TEST mode")
    @TestCaseId("SDK_TS_6")
    public void businessShouldBeAbleToCreateCredentialsAndUpdatePackageNameInTestMode() {
        // Step 1: Create a new project in TEST mode
        String projectName = StringUtils.getCompanyName();
        ProjectRequest projectRequest = new ProjectRequest(
                projectName,
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());

        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);

        String projectId = projectResponse.getId();
        Map<String, String> headers = restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId);

        // Step 2: Add initial Android credentials with a fingerprint
        CredentialsRequest.Fingerprint fingerprint = new CredentialsRequest.Fingerprint(
                StringUtils.getRandomFingerprint(),
                StringUtils.getRandomLabel());

        CredentialsRequest.Metadata metadata = new CredentialsRequest.Metadata(
                StringUtils.getPackageName(),
                Arrays.asList(fingerprint));

        CredentialsRequest credentialsRequest = CredentialsRequest.builder()
                .platform(ConstantStrings.PLATFORM.getMessage())
                .metadata(metadata)
                .build();

        CredentialsResponse credentialsResponse = restClient.post(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, headers, credentialsRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        String clientId = credentialsResponse.getClientId();
        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, clientId);

        // Step 3: Update the credentials (Package Name)
        CredentialsRequest.Fingerprint updatedFingerprint = new CredentialsRequest.Fingerprint(
                StringUtils.getRandomFingerprint(),
                StringUtils.getRandomLabel());

        String updatedPackageName = StringUtils.getPackageName();
        CredentialsRequest.Metadata updatedMetadata = new CredentialsRequest.Metadata(
                updatedPackageName,
                Arrays.asList(updatedFingerprint)
        );

        CredentialsRequest updatedCredentialsRequest = CredentialsRequest.builder()
                .platform(ConstantStrings.PLATFORM.getMessage())
                .metadata(updatedMetadata)
                .clientId(clientId)
                .build();

        restClient.patch(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), updatedCredentialsRequest, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        // Step 4: Get the updated credentials
        CredentialsResponse getUpdatedCredentialsResponse = restClient.get(serviceUrl, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        assertEquals(getUpdatedCredentialsResponse.getMetadata().getPackageName(), updatedPackageName);
    }

    @Test(description = "Business should be able to create credentials and update fingerprint in TEST mode")
    @TestCaseId("SDK_TS_7")
    public void businessShouldBeAbleToCreateCredentialsAndUpdateFingerprintInTestMode() {
        // Step 1: Create a new project in TEST mode
        String projectName = StringUtils.getCompanyName();
        ProjectRequest projectRequest = new ProjectRequest(
                projectName,
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());

        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);

        String projectId = projectResponse.getId();
        Map<String, String> headers = restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId);

        // Step 2: Add initial Android credentials with a fingerprint
        CredentialsRequest.Fingerprint initialFingerprint = new CredentialsRequest.Fingerprint(
                StringUtils.getRandomFingerprint(),
                StringUtils.getRandomLabel());

        CredentialsRequest.Metadata metadata = new CredentialsRequest.Metadata(
                StringUtils.getPackageName(),
                Arrays.asList(initialFingerprint));

        CredentialsRequest credentialsRequest = CredentialsRequest.builder()
                .platform(ConstantStrings.PLATFORM.getMessage())
                .metadata(metadata)
                .build();

        CredentialsResponse credentialsResponse = restClient.post(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, headers, credentialsRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        String clientId = credentialsResponse.getClientId();
        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, clientId);
        String pathParam = Constants.FINGERPRINT_PROPERTY;

        // Step 3: Update the fingerprint of the credentials
        String serviceUrlWithPathParam = RestClient.buildPathParamWithServiceUrl(
                PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V1, clientId, pathParam);

        UpdateCredentialRequest updateCredentialRequest = UpdateCredentialRequest.builder()
                .fingerPrint(initialFingerprint.getFingerPrint())
                .newFingerPrint(StringUtils.getRandomFingerprint())
                .newLabel(StringUtils.getRandomLabel())
                .build();

        restClient.patch(serviceUrlWithPathParam, ConstantStrings.CONTENT_TYPE.getMessage(), updateCredentialRequest, headers)
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

        // Step 4: Get the updated credentials
        CredentialsResponse getUpdatedCredentialsResponse = restClient.get(serviceUrl, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        // Assertion for the updated fingerprint
        assertThat(getUpdatedCredentialsResponse.getMetadata().getFingerPrints().stream().findFirst().get().getFingerPrint(),
                equalTo(updateCredentialRequest.getNewFingerPrint()));
    }

    @Test(description = "Business should be able to create credentials, add new fingerprint and delete fingerprint in TEST mode")
    @TestCaseId("SDK_TS_8")
    public void businessShouldBeAbleAddAndDeleteFingerprintSuccessfullyInTestMode() {
        // Step 1: Create a new project in TEST mode
        ProjectRequest projectRequest = new ProjectRequest(
                StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        Map<String, String> headers = restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectResponse.getId());

        // Step 2: Add initial Android credentials with a fingerprint
        CredentialsRequest.Fingerprint initialFingerprint = new CredentialsRequest.Fingerprint(
                StringUtils.getRandomFingerprint(),
                StringUtils.getRandomLabel());

        CredentialsRequest.Metadata metadata = new CredentialsRequest.Metadata(
                StringUtils.getPackageName(),
                Arrays.asList(initialFingerprint));

        CredentialsRequest credentialsRequest = CredentialsRequest.builder()
                .platform(ConstantStrings.PLATFORM.getMessage())
                .metadata(metadata)
                .build();

        CredentialsResponse credentialsResponse = restClient.post(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, headers, credentialsRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V2, credentialsResponse.getClientId());
        String serviceUrlWithPathParam = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_CREDENTIALS_ENDPOINT_V1, credentialsResponse.getClientId(), Constants.FINGERPRINT_PROPERTY);

        //Step 3: Add another fingerprint to the credentials
        FingerPrintRequest addAnotherFingerPrintRequest = FingerPrintRequest.builder()
                .fingerPrint(StringUtils.getRandomFingerprint())
                .label(StringUtils.getRandomLabel()).build();

        restClient.post(serviceUrlWithPathParam, headers, addAnotherFingerPrintRequest)
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

        // Step 4: Delete the fingerprint of the credentials
        FingerPrintRequest fingerPrintRequest = FingerPrintRequest.builder()
                .fingerPrint(initialFingerprint.getFingerPrint())
                .build();

        restClient.delete(serviceUrlWithPathParam, fingerPrintRequest, headers)
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

        // Step 5: Get the updated credentials
        CredentialsResponse getUpdatedCredentialsResponse = restClient.get(serviceUrl, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(CredentialsResponse.class);

        // Assertion for the added fingerprint
        assertThat(getUpdatedCredentialsResponse.getMetadata().getFingerPrints().stream().findFirst().get().getFingerPrint(),
                equalTo(addAnotherFingerPrintRequest.getFingerPrint()));
    }
}