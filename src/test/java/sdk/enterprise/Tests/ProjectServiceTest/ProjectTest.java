package sdk.enterprise.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Constants.ErrorCodes;
import sdk.enterprise.Constants.ErrorConstants;
import sdk.enterprise.CustomAnnotations.TestCaseId;
import sdk.enterprise.Entities.ErrorEntities.ErrorResponse;
import sdk.enterprise.Entities.RequestEntities.CategoryUpdateRequest;
import sdk.enterprise.Entities.RequestEntities.ProjectRequest;
import sdk.enterprise.Entities.ResponseEntities.CategoryUpdateResponse;
import sdk.enterprise.Entities.ResponseEntities.DetailsOfAllProjectsResponse;
import sdk.enterprise.Entities.ResponseEntities.ProjectResponse;
import sdk.enterprise.Utils.StringUtils;

import static org.testng.Assert.assertEquals;
import static sdk.enterprise.Utils.StringUtils.capitalise;

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
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectID))
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
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")))
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
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CategoryUpdateResponse.class);
        Assert.assertTrue(updateCategoryResponse.isUpdated());

        DetailsOfAllProjectsResponse getAllProjectResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1,
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId))
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
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("invalidProjectId")))
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
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId))
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
                        restClient.getHeaders(ConstantStrings.PROJECT_ID.getMessage(), projectId))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(errorResponse.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(errorResponse.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);
    }
}