package sdk.enterprise.Utils;

import com.github.javafaker.Faker;

public class StringUtils {
    private static final Faker faker = new Faker();

    public static String getCompanyName() {
        return faker.name().firstName();
    }

}
