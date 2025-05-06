package sdk.enterprise.Tests.PartnerServiceTest;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sdk.enterprise.Base.BaseTest;
import sdk.enterprise.Client.RestClient;
import sdk.enterprise.CustomAnnotations.TestCaseId;
import sdk.enterprise.DataProvider.ExcelDataProvider;
import sdk.enterprise.Entities.ResponseEntities.PartnerAccountResponse;
import static org.testng.Assert.assertEquals;

public class PartnerTest extends BaseTest {

    @BeforeMethod
    public void partnerSetup() {
        restClient = new RestClient(prop, baseURI);
    }

    @Test(description = "Validate the Get Account Details",
            dataProvider = "accountData", dataProviderClass = ExcelDataProvider.class)
    @TestCaseId("SDK_TC_092")
    public void businessShouldAbleToGetPartnerAccountDetails(String partnerId,
                                                             String email,
                                                             String country,
                                                             String contactFirstname,
                                                             String contactLastName,
                                                             String legalBusinessName)
    {
        PartnerAccountResponse partnerResponse = restClient.get(PARTNER_SERVICE_ENDPOINT_V1)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(PartnerAccountResponse.class);

        assertEquals(partnerResponse.getPartnerId(), partnerId);
        assertEquals(partnerResponse.getEmail(), email);
        assertEquals(partnerResponse.getCountry(), country);
        assertEquals(partnerResponse.getContactFirstName(), contactFirstname);
        assertEquals(partnerResponse.getContactLastName(), contactLastName);
        assertEquals(partnerResponse.getLegalBusinessName(), legalBusinessName);

    }
}

