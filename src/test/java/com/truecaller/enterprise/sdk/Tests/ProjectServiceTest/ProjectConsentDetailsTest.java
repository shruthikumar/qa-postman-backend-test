package com.truecaller.enterprise.sdk.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.truecaller.enterprise.sdk.Base.BaseTest;
import com.truecaller.enterprise.sdk.Client.RestClient;
import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Constants.Constants;
import com.truecaller.enterprise.sdk.CustomAnnotations.TestCaseId;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.ConsentRequest;
import com.truecaller.enterprise.sdk.Entities.RequestEntities.ProjectRequest;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.ConsentResponse;
import com.truecaller.enterprise.sdk.Entities.ResponseEntities.ProjectResponse;
import com.truecaller.enterprise.sdk.Utils.JsonPathValidator;
import com.truecaller.enterprise.sdk.Utils.StringUtils;

import java.io.File;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static com.truecaller.enterprise.sdk.Utils.StringUtils.getFileFromResources;

public class ProjectConsentDetailsTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        restClient = new RestClient(prop, baseURI);
    }

    @Test(description = "Business should be able to create and update consent details in TEST mode")
    @TestCaseId("SDK_TS_10")
    public void businessShouldBeAbletToCreateAndUpdateConsentDetails() {
        // Step 1: Create a new project in TEST mode
        String projectName = StringUtils.getCompanyName();
        ProjectRequest projectRequest = new ProjectRequest(
                projectName,
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());

        ProjectResponse projectResponse = restClient.post(PROJECT_SERVICE_ENDPOINT_V1, ConstantStrings.CONTENT_TYPE.getMessage(), projectRequest)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);

        Map<String, String> headers = restClient.setHeader(ConstantStrings.PROJECT_ID.getMessage(), projectResponse.getId());

        // Step 2: Create consent details for the project
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

        // Step 3 : Update the consent details
        ConsentRequest updatedConsentRequest = ConsentRequest.builder()
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

        String updatedConsentJson = JsonPathValidator.convertObjectToJsonString(updatedConsentRequest);

        ConsentResponse updatedConsentResponse = restClient.postMultiPart(PROJECT_SERVICE_CONSENT_ENDPOINT_V1, headers, appLogoFile, updatedConsentJson)
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().as(ConsentResponse.class);
        assertEquals(updatedConsentResponse.getSummary(), Constants.CONSENT_SCREEN_SAVED);

        // Step 4: Validate the consent details
        ConsentResponse consentDetailsResponse = restClient.get(PROJECT_SERVICE_CONSENT_ENDPOINT_V2, headers)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(ConsentResponse.class);

        assertEquals(consentDetailsResponse.getAppName(), updatedConsentRequest.getAppName());
        assertEquals(consentDetailsResponse.getDeveloperEmail(), updatedConsentRequest.getDeveloperEmail());
        assertEquals(consentDetailsResponse.getDeveloperName(), updatedConsentRequest.getDeveloperName());
        assertEquals(consentDetailsResponse.getMandatoryScopes(), updatedConsentRequest.getMandatoryScopes());
        assertEquals(consentDetailsResponse.getUserSupportEmail(), updatedConsentRequest.getUserSupportEmail());
        assertEquals(consentDetailsResponse.getPrivacyPolicyUrl(), updatedConsentRequest.getPrivacyPolicyUrl());
        assertEquals(consentDetailsResponse.getTosUrl(), updatedConsentRequest.getTosUrl());
        assertEquals(consentDetailsResponse.getHomePageUrl(), updatedConsentRequest.getHomePageUrl());
    }
}