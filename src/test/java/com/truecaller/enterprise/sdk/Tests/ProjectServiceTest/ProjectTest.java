package com.truecaller.enterprise.sdk.Tests.ProjectServiceTest;

import com.truecaller.enterprise.sdk.Client.RestClient;
import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.TestPhoneNumberRequest;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.ProjectResponse;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.truecaller.enterprise.sdk.Base.BaseTest;
import com.truecaller.enterprise.sdk.Constants.ErrorCodes;
import com.truecaller.enterprise.sdk.Constants.ErrorConstants;
import com.truecaller.enterprise.sdk.CustomAnnotations.TestCaseId;
import com.truecaller.enterprise.sdk.Entities.ErrorEntities.ErrorResponse;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.CategoryUpdateRequest;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.ProjectRequest;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.CategoryUpdateResponse;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.DeactivateProjectResponse;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.DetailsOfAllProjectsResponse;
import com.truecaller.enterprise.sdk.Utils.StringUtils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static com.truecaller.enterprise.sdk.Utils.StringUtils.capitalise;
import static com.truecaller.enterprise.sdk.Utils.StringUtils.getRandomMobileNumber;

public class ProjectTest extends BaseTest {

    @BeforeMethod
    public void projectSetup() {
        restClient = new RestClient(prop, baseURI);
    }


    @Test(description = "Create project with the valid request body")
    @TestCaseId("SDK_TC_009")
    public void businessShouldAbleToCreateProject() {
        ProjectRequest project = new ProjectRequest(StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
    }

    @Test(description = "Validate the Error Message for Empty Name in Create Project")
    @TestCaseId("SDK_TC_015")
    public void shouldThrowErrorForEmptyNameInCreateProject() {
        ProjectRequest project = new ProjectRequest("", ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);

    }

    @Test(description = "Validate the Error Message for Empty Business Category in Create Project")
    @TestCaseId("SDK_TC_012")
    public void ShouldThrowErrorForEmptyBusinessCategoryInCreateProject() {
        ProjectRequest project = new ProjectRequest(StringUtils.getCompanyName(), "");
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);

    }

    @Test(description = "Validate the Error Message for Empty Name And Empty Business Category in Create Project")
    @TestCaseId("SDK_TC_013")
    public void ShouldThrowErrorForEmptyNameAndEmptyBusinessCategoryInCreateProject() {
        ProjectRequest project = new ProjectRequest("", "");
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);

    }

    @Test(description = "Validate the Error Message for Duplicate Name in Create Project")
    @TestCaseId("SDK_TC_010")
    public void ShouldThrowErrorForDuplicateNameInCreateProject() {
        String projectName = StringUtils.getCompanyName();
        ProjectRequest project = new ProjectRequest(projectName, ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_OK);

        ProjectRequest duplicateProject = new ProjectRequest(projectName, ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), duplicateProject)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.DUPLICATE_NAME_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.DUPLICATE_NAME_MSG);
    }

    @Test(description = "Validate the error message returned when a Create Project request is made without a request body")
    @TestCaseId("SDK_TC_014")
    public void ShouldThrowErrorForEmptyBodyRequestInCreateProject() {
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage())
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.INVALID_JSON_DATA_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.INVALID_JSON_DATA_MSG);
    }

    @Test(description = "Get project details for the given ProjectID")
    @TestCaseId("SDK_TC_016")
    public void businessShouldBeAbleToViewDetailsOfSpecificProject() {
        String projectName = StringUtils.getCompanyName();
        ProjectRequest projectRequest = new ProjectRequest(projectName, ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage()
        );
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectID = projectResponse.getId();

        DetailsOfAllProjectsResponse response = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectID))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);
        assertEquals(response.getId(), projectID);
        assertEquals(response.getName(), projectName);
        assertEquals(response.getMode(), ConstantStrings.TEST_MODE.getMessage());
        assertEquals(response.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());
        assertEquals(response.getBusinessCategory().getId(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        assertEquals(response.getBusinessCategory().getLabel(), capitalise(ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage()));
    }

    @Test(description = "Get project details for Invalid ProjectID")
    @TestCaseId("SDK_TC_017")
    public void shouldThrowAnErrorForInvalidProjectIdInGetProjectDetails() {
        ErrorResponse response = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")))
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.INVALID_PROJECT_ID_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.INVALID_PROJECT_ID_ERROR_MSG);
    }

    @Test(description = "Update Business Category with valid request body")
    @TestCaseId("SDK-TC_017")
    public void businessShouldBeAbleToUpdateBusinessCategory() {
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse ProjectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = ProjectResponse.getId();

        CategoryUpdateRequest updateCategoryRequest = new CategoryUpdateRequest(ConstantStrings.BUSINESS_CATEGORY_REALTY.getMessage());
        CategoryUpdateResponse updateCategoryResponse = restClient.put(PROJECT_SERVICE_CATEGORY_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), updateCategoryRequest,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CategoryUpdateResponse.class);
        assertTrue(updateCategoryResponse.isUpdated());

        DetailsOfAllProjectsResponse getAllProjectResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);
        assertEquals(getAllProjectResponse.getBusinessCategory().getId(), ConstantStrings.BUSINESS_CATEGORY_REALTY.getMessage());
        assertEquals(getAllProjectResponse.getBusinessCategory().getLabel(), capitalise(ConstantStrings.BUSINESS_CATEGORY_REALTY.getMessage()));
    }

    @Test(description = "Update Business Category with Invalid ProjectID")
    @TestCaseId("SDK_TC_018")
    public void shouldThroughAnErrorForInvalidProjectIdInUpdateBusinessCategory() {
        CategoryUpdateRequest updateCategoryRequest = new CategoryUpdateRequest(ConstantStrings.BUSINESS_CATEGORY_REALTY.getMessage());
        ErrorResponse updateCategoryResponse = restClient.put(PROJECT_SERVICE_CATEGORY_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), updateCategoryRequest,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")))
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract().as(ErrorResponse.class);
        assertEquals(updateCategoryResponse.getStatus(), ErrorCodes.INVALID_PROJECT_ID_ERROR_CODE);
        assertEquals(updateCategoryResponse.getMessage(), ErrorConstants.INVALID_PROJECT_ID_ERROR_MSG);
    }

    @Test(description = "Validate the Error Message for Invalid Business Category in Update Business Category")
    @TestCaseId("SDK_TC_021")
    public void ShouldThrowErrorForInvalidBusinessCategoryInUpdateBusinessCategory() {
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse ProjectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = ProjectResponse.getId();

        CategoryUpdateRequest updateCategoryRequest = new CategoryUpdateRequest(ConstantStrings.TEST_MODE.getMessage());
        ErrorResponse errorResponse = restClient.put(PROJECT_SERVICE_CATEGORY_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), updateCategoryRequest,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);
    }

    @Test(description = "Validate the Error Message for null value Business Category in Update Business Category")
    @TestCaseId("SDK_TC_022")
    public void ShouldThrowErrorForNullBusinessCategoryInUpdateBusinessCategory() {
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse ProjectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = ProjectResponse.getId();

        CategoryUpdateRequest updateCategoryRequest = new CategoryUpdateRequest(null);
        ErrorResponse errorResponse = restClient.put(PROJECT_SERVICE_CATEGORY_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), updateCategoryRequest,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);
    }

    @Test(description = "Add mobile number to the specific project and validate the added Phone number")
    @TestCaseId("SDK_TC_035")
    public void businessShouldBeAbleToAddTestPhoneNumberToProject() {
        //Step - 1 : Create Project
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = projectResponse.getId();

        //Step - 2 :Add Test Phone number to Project
        TestPhoneNumberRequest testPhoneNumberRequestBody = TestPhoneNumberRequest.builder()
                .phoneNumber(getRandomMobileNumber())
                .build();
        restClient.post(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId), testPhoneNumberRequestBody)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        //Step - 3 : Fetch the phone Number which was added
        String phoneNumberResponseBody = restClient.get(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
        assertTrue(phoneNumberResponseBody.contains(String.valueOf(testPhoneNumberRequestBody.getPhoneNumber())));
    }

    @Test(description = "Verify an error response when exceeding max test phone numbers")
    @TestCaseId("SDK_TC_036")
    public void shouldThrowErrorWhenExceedingMaxTestPhoneNumbers() {
        TestPhoneNumberRequest testPhoneNumberRequest = TestPhoneNumberRequest.builder()
                .phoneNumber(getRandomMobileNumber())
                .build();
        ErrorResponse errorResponse = restClient.post(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("maxPhoneNumberLimitProjectId")), testPhoneNumberRequest)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.PHONE_NUMBER_LIMIT_EXCEEDED_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.PHONE_NUMBER_LIMIT_EXCEEDED_ERROR_MSG);
    }

    @Test(description = "Verify an error response with Invalid project ID")
    @TestCaseId("SDK_TC_037")
    public void ShouldThrowAnErrorForInvalidProjectIdInAddTestPhoneNumbers() {
        TestPhoneNumberRequest testPhoneNumberRequestBody = TestPhoneNumberRequest.builder()
                .phoneNumber(getRandomMobileNumber())
                .build();
        ErrorResponse errorResponse = restClient.post(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")), testPhoneNumberRequestBody)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.INVALID_PROJECT_ID_PHONE_NUMBER_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.INVALID_PROJECT_ID_ERROR_MSG);
    }

    @Test(description = "Deactivate the Project which is in Active Status")
    @TestCaseId("SDK_TC_028")
    public void businessShouldBeAbleToDeactivateProject() {
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = projectResponse.getId();

        DeactivateProjectResponse deactivateProjectResponse = restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(DeactivateProjectResponse.class);
        assertEquals(deactivateProjectResponse.isDeactivated(), true);
    }

    @Test(description = "Validate the Error Message for Invalid ProjectId in Deactivate Project")
    @TestCaseId("SC_TC_029")
    public void ShouldThrowErrorForInvalidProjectIdInDeactivateProject() {
        ErrorResponse errorResponse = restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")))
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.INVALID_PROJECT_ID_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.INVALID_PROJECT_ID_MSG);
    }

    @Test(description = "Validate the error message for the deactivated project")
    @TestCaseId("SDK_TC_030")
    public void shouldThrowErrorForWhenProjectIsAlreadyDeactivated() {
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = projectResponse.getId();

        DeactivateProjectResponse deactivateProjectResponse = restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(DeactivateProjectResponse.class);
        assertEquals(deactivateProjectResponse.isDeactivated(), true);

        ErrorResponse errorResponse = restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.INVALID_PROJECT_STATUS_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.INVALID_PROJECT_STATUS_ERROR_MSG);
    }

    @Test(description = "Validate Delete phone number from the project")
    @TestCaseId("SDK_TC_041")
    public void businessShouldBeAbleToDeleteTestPhoneNumberFromProject() {
        //Step 1 : Create Project
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = projectResponse.getId();

        //Step 2 :Add Test Phone number to Project
        TestPhoneNumberRequest testPhoneNumberRequestBody = TestPhoneNumberRequest.builder()
                .phoneNumber(getRandomMobileNumber())
                .build();
        restClient.post(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId), testPhoneNumberRequestBody)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        //Step 3 : Verify the phone Number which was added
        String fetchPhoneNumberResponseBody = restClient.get(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
        assertTrue(fetchPhoneNumberResponseBody.contains(String.valueOf(testPhoneNumberRequestBody.getPhoneNumber())));

        //Step 4: Delete The phone number
        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1, testPhoneNumberRequestBody.getPhoneNumber());
        Boolean deletePhoneNumberResponse = restClient.delete(serviceUrl, restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Boolean.class);
        Assert.assertTrue(deletePhoneNumberResponse);
    }

    @Test(description = "Validate an Error Message to Delete the same Phone Number more than once")
    @TestCaseId("SDK_TC_043")
    public void shouldThrowWhenDeletingPhoneNumberMultipleTimes() {
        //Step 1 : Create Project
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
        String projectId = projectResponse.getId();

        //Step 2 :Add Test Phone number to Project
        TestPhoneNumberRequest testPhoneNumberRequestBody = TestPhoneNumberRequest.builder()
                .phoneNumber(getRandomMobileNumber())
                .build();
        restClient.post(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId), testPhoneNumberRequestBody)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        //Step 3 : Verify the phone Number which was added
        String fetchPhoneNumberResponseBody = restClient.get(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1,
                        restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
        assertTrue(fetchPhoneNumberResponseBody.contains(String.valueOf(testPhoneNumberRequestBody.getPhoneNumber())));

        //Step 4: Delete The phone number
        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1, testPhoneNumberRequestBody.getPhoneNumber());
        Boolean deletePhoneNumberResponse = restClient.delete(serviceUrl, restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Boolean.class);
        Assert.assertTrue(deletePhoneNumberResponse);

        //Step 5: Delete Same Number again
        ErrorResponse errorResponse = restClient.delete(serviceUrl, restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.INVALID_PHONE_NUMBER_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.INVALID_PHONE_NUMBER_ERROR_MSG);
    }

    @Test(description = "Validate Error Message for Invalid Project Id In delete test Phone Number")
    @TestCaseId("SDK_TC_044")
    public void shouldThrowAnErrorForInvalidProjectIdInDeleteTestPhoneNumber() {
        String serviceUrl = RestClient.buildPathParamWithServiceUrl(PROJECT_SERVICE_TEST_PHONE_NUMBER_ENDPOINT_V1, getRandomMobileNumber());
        ErrorResponse errorResponse = restClient.delete(serviceUrl, restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")))
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.INVALID_PROJECT_ID_PHONE_NUMBER_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.INVALID_PROJECT_ID_MSG);
    }
}