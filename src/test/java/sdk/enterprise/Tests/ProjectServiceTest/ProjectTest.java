package sdk.enterprise.Tests.ProjectServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Entities.RequestEntities.ProjectRequest;
import sdk.enterprise.Entities.ResponseEntities.ProjectResponse;
import sdk.enterprise.Utils.StringUtils;

public class ProjectTest extends BaseTest {
    @BeforeMethod
    public void projectSetup() {
        restClient = new RestClient(prop, baseURI);
    }


    @Test(description = "Create project with the valid request body")
    public void businessShouldAbleToCreateProject() {
        ProjectRequest project = new ProjectRequest(StringUtils.getCompanyName(),
                ConstantStrings.BUSINESS_CATEGORY_GAMING.getMessage());
        restClient.post(PROJECT_SERVICE_ENDPOINT,ConstantStrings.CONTENT_TYPE.getMessage(), project,true)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(ProjectResponse.class);
    }


}
