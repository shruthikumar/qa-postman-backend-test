package sdk.enterprise.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.CustomAnnotations.TestCaseId;
import sdk.enterprise.Entities.RequestEntities.CredentialsRequest;
import sdk.enterprise.Entities.RequestEntities.ProjectRequest;
import sdk.enterprise.Entities.ResponseEntities.CredentialsResponse;
import sdk.enterprise.Entities.ResponseEntities.ProjectResponse;
import sdk.enterprise.Utils.StringUtils;

import java.util.Arrays;
import java.util.Map;

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
}