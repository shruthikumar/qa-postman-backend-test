package com.truecaller.enterprise.sdk.Utils;

import com.github.javafaker.Faker;
import com.truecaller.enterprise.sdk.Constants.ConstantStrings;
import com.truecaller.enterprise.sdk.Constants.Constants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Random;

public class StringUtils {
    private static final Faker faker = new Faker();

    public static String getCompanyName() {
        String companyName = "SDK Automation";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return companyName + "  " + timestamp;
    }

    public static String getFirstName() {
        return faker.name().firstName();
    }

    /**
     * Returns a File object for a file located in src/test/resources or src/main/resources.
     *
     * @param fileName name of the file with relative path (e.g., "data/sample.png")
     * @return File object
     */
    public static File getFileFromResources(String fileName) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        return new File(resource.getFile());
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getFullName() {
        return faker.name().fullName().replaceAll("[^a-zA-Z ]", "");
    }

    public static String getEmail() {
        return faker.name().username().toLowerCase() + "@truecaller.com";
    }

    public static String getAppName() {
        return faker.app().name();
    }

    public static String getPackageName() {
        return "com.example." + faker.lorem().word().toLowerCase();
    }

    public static String getRandomFingerprint() {
        return faker.crypto().sha256();
    }

    public static String getRandomLabel() {
        return faker.lorem().word();
    }

    public static String getPrivacyPolicyUrl() {
        return Constants.TC_URL + faker.lorem().word().toLowerCase();
    }

    public static String getTosUrl() {
        return Constants.TC_URL + faker.lorem().word().toLowerCase();
    }

    public static String getHomePageUrl() {
        return Constants.TC_URL + faker.lorem().word().toLowerCase();
    }

    public static long getRandomMobileNumber() {
        return Long.parseLong("91" + faker.phoneNumber().subscriberNumber(8));
    }

    public static String getRandomParagraph() {
        return faker.lorem().paragraph();
    }

    public static String capitalise(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String getRandomBusinessCategory() {
        ConstantStrings.BusinessCategory[] values = ConstantStrings.BusinessCategory.values();
        int randomIndex = new Random().nextInt(values.length);
        return values[randomIndex].name();
    }
}