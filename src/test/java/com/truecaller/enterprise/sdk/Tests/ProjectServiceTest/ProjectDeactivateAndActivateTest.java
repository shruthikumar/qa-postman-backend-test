package com.truecaller.enterprise.sdk.Tests.ProjectServiceTest;

import com.truecaller.enterprise.sdk.Base.BaseTest;
import com.truecaller.enterprise.sdk.Client.RestClient;
import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.ProjectRequest;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.DetailsOfAllProjectsResponse;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.ProjectResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.truecaller.enterprise.sdk.CustomAnnotations.TestCaseId;
import com.truecaller.enterprise.sdk.Utils.StringUtils;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ProjectDeactivateAndActivateTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        restClient = new RestClient(prop, baseURI);
    }
    @Test(description = "Business should be able to deactivate and activate a project in TEST mode")
    @TestCaseId("SDK_TS_3")
    public void businessShouldBeAbleToDeactivateAndActivateProjectInTestMode() {
        // Step 1: Create a new project in TEST mode
        ProjectRequest projectRequest = new ProjectRequest(
                StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());

        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1,
                        ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);

        String projectId = projectResponse.getId();
        Map<String, String> headers = restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectId);

        // Step 2: Verify initial project status is ACTIVE
        DetailsOfAllProjectsResponse initialDetailsResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(initialDetailsResponse.getMode(), ConstantStrings.TEST_MODE.getMessage());
        assertEquals(initialDetailsResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());

        // Step 3: Deactivate the project
        restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK);

        // Step 4: Verify project status is INACTIVE
        DetailsOfAllProjectsResponse afterDeactivationResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(afterDeactivationResponse.getStatus(), ConstantStrings.STATUS_INACTIVE.getMessage());

        // Step 5: Reactivate the project
        restClient.put(PROJECT_SERVICE_ACTIVATE_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK);

        // Step 6: Verify project status is ACTIVE again
        DetailsOfAllProjectsResponse afterActivationResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(afterActivationResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());
    }

    @Test(description = "Business should be able to deactivate and activate a project in REVIEW mode")
    @TestCaseId("SDK_TS_4")
    public void businessShouldBeAbleToDeactivateAndActivateProjectInReviewMode() {
        Map<String, String> headers = restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("projectIdInReview"));

        // Step 1: Verify initial project status is ACTIVE
        DetailsOfAllProjectsResponse initialDetailsResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(initialDetailsResponse.getMode(), ConstantStrings.REVIEW_MODE.getMessage());
        assertEquals(initialDetailsResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());

        // Step 2: Deactivate the project
        restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK);

        // Step 3: Verify project status is INACTIVE
        DetailsOfAllProjectsResponse afterDeactivationResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(afterDeactivationResponse.getStatus(), ConstantStrings.STATUS_INACTIVE.getMessage());

        // Step 4: Reactivate the project
        restClient.put(PROJECT_SERVICE_ACTIVATE_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK);

        // Step 5: Verify project status is ACTIVE again
        DetailsOfAllProjectsResponse afterActivationResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(afterActivationResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());
    }

//    @Test(description = "Business should be able to deactivate and activate a project in PRODUCTION mode")
//    @TestCaseId("SDK_TS_5")
//    public void businessShouldBeAbleToDeactivateAndActivateProjectInProductionMode() {
//        Map<String, String> headers = restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), prop.getProperty("projectIdInProduction"));
//
//        // Step 1: Verify initial project status is ACTIVE
//        DetailsOfAllProjectsResponse initialDetailsResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
//                .then().statusCode(HttpStatus.SC_OK)
//                .extract().as(DetailsOfAllProjectsResponse.class);
//
//        assertEquals(initialDetailsResponse.getMode(), ConstantStrings.PRODUCTION_MODE.getMessage());
//        assertEquals(initialDetailsResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());
//
//        // Step 2: Deactivate the project
//        restClient.put(PROJECT_SERVICE_DEACTIVATE_ENDPOINT_V1, headers)
//                .then().statusCode(HttpStatus.SC_OK);
//
//        // Step 3: Verify project status is INACTIVE
//        DetailsOfAllProjectsResponse afterDeactivationResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
//                .then().statusCode(HttpStatus.SC_OK)
//                .extract().as(DetailsOfAllProjectsResponse.class);
//
//        assertEquals(afterDeactivationResponse.getStatus(), ConstantStrings.STATUS_INACTIVE.getMessage());
//
//        // Step 4: Reactivate the project
//        restClient.put(PROJECT_SERVICE_ACTIVATE_ENDPOINT_V1, headers)
//                .then().statusCode(HttpStatus.SC_OK);
//
//        // Step 5: Verify project status is ACTIVE again in prod
//        DetailsOfAllProjectsResponse afterActivationResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
//                .then().statusCode(HttpStatus.SC_OK)
//                .extract().as(DetailsOfAllProjectsResponse.class);
//
//        assertEquals(afterActivationResponse.getStatus(), ConstantStrings.STATUS_ACTIVE.getMessage());
//    }
}