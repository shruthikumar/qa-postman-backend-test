package com.truecaller.enterprise.sdk.DataProvider;

import org.testng.annotations.DataProvider;
import com.truecaller.enterprise.sdk.Constants.Constants;
import com.truecaller.enterprise.sdk.Utils.ExcelUtil;

public class ExcelDataProvider {

    @DataProvider(name = "accountData")
    public Object[][] getAccountData() {
        return ExcelUtil.getTestData(Constants.ACCOUNT_SHEET_NAME);
    }

}
