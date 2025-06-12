package com.truecaller.enterprise.sdk.Tests.ProjectServiceTest;

import com.truecaller.enterprise.sdk.Client.RestClient;
import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.ProjectRequest;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.DetailsOfAllProjectsResponse;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.ProjectResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.truecaller.enterprise.sdk.Base.BaseTest;
import com.truecaller.enterprise.sdk.CustomAnnotations.TestCaseId;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.UpdateProjectCategoryRequest;
import com.truecaller.enterprise.sdk.Utils.StringUtils;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ProjectCategoryTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        restClient = new RestClient(prop, baseURI);
    }

    @Test(description = "Business should be able to create a project with a random category and update it with a different random category in TEST mode")
    @TestCaseId("SDK_TS_12")
    public void businessShouldBeAbleToCreateAndUpdateProjectWithRandomCategory() {
        // Step 1: Create project with random category
        String initialCategory = StringUtils.getRandomBusinessCategory();
        ProjectRequest projectRequest = new ProjectRequest(StringUtils.getCompanyName(), initialCategory);
        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);

        Map<String, String> headers = restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectResponse.getId());

        // Step 2: Validate initial category in project details
        DetailsOfAllProjectsResponse detailsOfAllProjectsResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(detailsOfAllProjectsResponse.getBusinessCategory().getId(), initialCategory);

        // Step 3: Update category with another random category
        String updatedCategory = StringUtils.getRandomBusinessCategory();
        UpdateProjectCategoryRequest updateProjectCategoryRequest = new UpdateProjectCategoryRequest(updatedCategory);
        restClient.put(PROJECT_SERVICE_CATEGORY_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), updateProjectCategoryRequest, headers)
                .then().statusCode(HttpStatus.SC_OK);

        // Step 4: Validate updated category in project details
        DetailsOfAllProjectsResponse updatedProjectDetailsResponse = restClient.get(PROJECT_SERVICE_DETAILS_ENDPOINT_V1, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(DetailsOfAllProjectsResponse.class);

        assertEquals(updatedProjectDetailsResponse.getBusinessCategory().getId(), updatedCategory);
    }
}