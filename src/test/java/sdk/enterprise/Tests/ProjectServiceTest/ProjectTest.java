package sdk.enterprise.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Constants.ErrorCodes;
import sdk.enterprise.Constants.ErrorConstants;
import sdk.enterprise.CustomAnnotations.TestCaseId;
import sdk.enterprise.Entities.ErrorEntities.ErrorResponse;
import sdk.enterprise.Entities.RequestEntities.ProjectRequest;
import sdk.enterprise.Entities.ResponseEntities.ProjectResponse;
import sdk.enterprise.Utils.StringUtils;

import static org.testng.Assert.assertEquals;

public class ProjectTest extends BaseTest {

    @BeforeMethod
    public void projectSetup() {
        restClient = new RestClient(prop, baseURI);
    }


    @Test(description = "Create project with the valid request body")
    @TestCaseId("SDK_TC_012")
    public void businessShouldAbleToCreateProject() {
        ProjectRequest project = new ProjectRequest(StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        restClient.post(PROJECT_SERVICE_ENDPOINT, ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
    }

    @Test(description = "Validate the Error Message for Empty Name in Create Project")
    @TestCaseId("SDK_TC_015")
    public void shouldThrowErrorForEmptyNameInCreateProject() {
        ProjectRequest project = new ProjectRequest("", ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT,
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
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT,
                        ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.EMPTY_NAME_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.EMPTY_NAME_ERROR__MSG);

    }

    @Test(description = "Validate the Error Message for Empty Name And Empty Business Category in Create Project")
    @TestCaseId("SDK_TC_012")
    public void ShouldThrowErrorForEmptyNameAndEmptyBusinessCategoryInCreateProject() {
        ProjectRequest project = new ProjectRequest("", "");
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT,
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
        restClient.post(PROJECT_SERVICE_ENDPOINT, ConstantStrings.CONTENT_TYPE.getMessage(), project)
                .then().statusCode(HttpStatus.SC_OK);

        ProjectRequest duplicateProject = new ProjectRequest(projectName, ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT,
                        ConstantStrings.CONTENT_TYPE.getMessage(), duplicateProject)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.DUPLICATE_NAME_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.DUPLICATE_NAME_MSG);
    }

    @Test(description = "Validate the error message returned when a Create Project request is made without a request body")
    @TestCaseId("SDK_TC_014")
    public void ShouldThrowErrorForEmptyBodyRequestInCreateProject() {
        ErrorResponse response = restClient.post(PROJECT_SERVICE_ENDPOINT,
                        ConstantStrings.CONTENT_TYPE.getMessage())
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().as(ErrorResponse.class);
        assertEquals(response.getStatus(), ErrorCodes.INVALID_JSON_DATA_ERROR_CODE);
        assertEquals(response.getMessage(), ErrorConstants.INVALID_JSON_DATA_MSG);
    }
}
