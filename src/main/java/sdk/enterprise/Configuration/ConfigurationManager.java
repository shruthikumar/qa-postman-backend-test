package sdk.enterprise.Configuration;

import sdk.enterprise.FrameworkException.APIFrameworkException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private Properties property;
    private FileInputStream input;

    public Properties initProp() {
        property = new Properties();
        String envName = System.getProperty("env");

        try {
            if (envName == null) {
                input = new FileInputStream("./src/test/resources/config/qa.config.properties");
            } else {

                switch (envName.toLowerCase().trim()) {
                    case "qa":
                        input = new FileInputStream("./src/test/resources/Config/qa.config.properties");
                        break;
                    case "dev":
                        input = new FileInputStream("./src/test/resources/Config/dev.config.properties");
                        break;
                    case "stage":
                        input = new FileInputStream("./src/test/resources/Config/stage.config.properties");
                        break;
                    case "prod":
                        input = new FileInputStream("./src/test/resources/Config/config.properties");
                        break;

                    default:
                        throw new APIFrameworkException("WRONG ENV IS Given");
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        try {
            property.load(input);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return property;

    }
}
