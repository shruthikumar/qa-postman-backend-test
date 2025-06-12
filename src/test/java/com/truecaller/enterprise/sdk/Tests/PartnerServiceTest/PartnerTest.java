package com.truecaller.enterprise.sdk.Tests.PartnerServiceTest;

import com.truecaller.enterprise.sdk.Entities.ResponseEntities.PartnerAccountResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.truecaller.enterprise.sdk.Base.BaseTest;
import com.truecaller.enterprise.sdk.Client.RestClient;
import com.truecaller.enterprise.sdk.CustomAnnotations.TestCaseId;
import com.truecaller.enterprise.sdk.DataProvider.ExcelDataProvider;

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

