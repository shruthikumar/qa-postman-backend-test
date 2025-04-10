package sdk.enterprise.Utils;

import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class StringUtils {
    private static final Faker faker = new Faker();

    public static String getCompanyName() {
        return faker.name().firstName();
    }

    /**
     * Returns a File object for a file located in src/test/resources or src/main/resources.
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

}
