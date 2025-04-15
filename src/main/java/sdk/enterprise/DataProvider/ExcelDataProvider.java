package sdk.enterprise.DataProvider;

import org.testng.annotations.DataProvider;
import sdk.enterprise.Constants.Constants;
import sdk.enterprise.Utils.ExcelUtil;

public class ExcelDataProvider {

    @DataProvider(name = "accountData")
    public Object[][] getAccountData() {
        return ExcelUtil.getTestData(Constants.ACCOUNT_SHEET_NAME);
    }

}
