package com.truecaller.enterprise.sdk.Utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.truecaller.enterprise.sdk.FrameworkException.APIFrameworkException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class JsonPathValidator {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String getJsonResponseAsString(Response response) {
        return response.getBody().asString();
    }


    public <T> T read(Response response, String jsonPath) {
        String jsonResponse =  getJsonResponseAsString(response);
        try {
            return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
            e.printStackTrace();
            throw new APIFrameworkException(jsonPath + "is not found...");
        }
    }

    public <T> List<T> readList(Response response, String jsonPath) {
        String jsonResponse =  getJsonResponseAsString(response);
        try {
            return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
            e.printStackTrace();
            throw new APIFrameworkException(jsonPath + "is not found...");
        }
    }


    public <T> List<Map<String, T>> readListOfMaps(Response response, String jsonPath) {
        String jsonResponse =  getJsonResponseAsString(response);
        try {
            return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
            e.printStackTrace();
            throw new APIFrameworkException(jsonPath + "is not found...");
        }
    }

    public static String convertObjectToJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON string", e);
        }
    }
}
