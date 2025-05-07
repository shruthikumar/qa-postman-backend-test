package sdk.enterprise.Client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import sdk.enterprise.Constants.ConstantStrings;
import sdk.enterprise.Constants.Constants;
import sdk.enterprise.Entities.RequestEntities.LoginRequest;
import sdk.enterprise.FrameworkException.APIFrameworkException;
import sdk.enterprise.Utils.TokenManager;

import static io.restassured.RestAssured.given;


public class RestClient {

    private RequestSpecBuilder specBuilder;

    private Properties prop;
    private String baseURI;


    /**
     * Initializes a new RestClient with prop and baseUri
     *
     * @param prop    takes property file
     * @param baseURI takes base uri
     */
    public RestClient(Properties prop, String baseURI) {
        specBuilder = new RequestSpecBuilder();
        this.prop = prop;
        this.baseURI = baseURI;
    }

    /**
     * Add Authorization header with Bearer Token
     */
    public void addAuthorizationHeader(RequestSpecBuilder builder) {
        builder.addHeader("Authorization", "Bearer " + TokenManager.getToken());

    }

    /**
     * get the headers will set given key and value in the Map and return as Map
     *
     * @param key   takes key as String
     * @param value takes key as String
     * @return headers Map
     */
    public Map<String, String> getHeaders(String key, String value) {
        Map<String, String> headers = new HashMap<>();
        headers.put(key, value);
        return headers;
    }

    /**
     * Sets the Request Content Type Based on given type
     *
     * @param contentType should be String mentioned in Switch
     */
    private void setRequestContentType(String contentType) {
        switch (contentType.toLowerCase()) {
            case "json":
                specBuilder.setContentType(ContentType.JSON);
                break;
            case "xml":
                specBuilder.setContentType(ContentType.XML);
                break;
            case "text":
                specBuilder.setContentType(ContentType.TEXT);
                break;
            case "multipart":
                specBuilder.setContentType(ContentType.MULTIPART);
                break;

            default:
                throw new APIFrameworkException("INVALID CONTENT TYPE");
        }
    }

    /**
     * Sets the Request And Response Logging
     *
     * @param specBuilder which takes Spec object
     */
    private void setLogging(RequestSpecBuilder specBuilder ) {
        specBuilder.addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL));
    }

    /**
     * Create Request Specification without parameter
     *
     * @return Request Specification object
     */
    private RequestSpecification createRequestSpec() {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);
        setLogging(specBuilder);
        return specBuilder.build();
    }

    /**
     * Create Request Specification with specific Headers
     *
     * @param headersMap takes Multiple headers in key value format
     * @return Request Specification Object
     */
    private RequestSpecification createRequestSpec(Map<String, String> headersMap) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);

        if (headersMap != null) {
            specBuilder.addHeaders(headersMap);
        }
        setLogging(specBuilder);
        return specBuilder.build();
    }

    /**
     * Builds a clean URL path by joining individual segments.
     * Trims any leading or trailing slashes from each segment and joins them with a single slash.
     *
     * @param segments The individual path segments to be joined together.
     * @return A normalized URL path starting with a single
     */
    public static String buildPathParamWithServiceUrl(String serviceUrl,String... segments) {
        StringBuilder path = new StringBuilder();
        for (String segment : segments) {
            if (segment != null) {
                segment = segment.replaceAll("^/+", "").replaceAll("/+$", "");
                path.append("/").append(segment);
            }
        }
        return serviceUrl + path;
    }

    /**
     * Create RequestSpecification based on given headers Map and QueryParams
     *
     * @param headersMap  takes Multiple  headers in the form of map
     * @param queryParams takes Multiple Query params in the form of map
     * @return request Specification Object
     */
    private RequestSpecification createRequestSpec(Map<String, String> headersMap, Map<String, Object> queryParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);

        if (headersMap != null) {
            specBuilder.addHeaders(headersMap);
        }
        if (queryParams != null) {
            specBuilder.addQueryParams(queryParams);
        }
        setLogging(specBuilder);
        return specBuilder.build();
    }

    /**
     * CreateRequestSpec based on request body and content type
     *
     * @param requestBody takes request of type object
     * @param contentType takes content type of type string
     * @return request Specification object
     */
    private RequestSpecification createRequestSpec(Object requestBody, String contentType) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);

        setRequestContentType(contentType);

        if (requestBody != null) {
            specBuilder.setBody(requestBody);
        }
        setLogging(specBuilder);
        return specBuilder.build();
    }
    /**
     * CreateRequestSpec based on request body and content type
     *
     * @param requestBody takes request of type object
     * @param headersMap takes map as key and value
     * @return request Specification object
     */
    private RequestSpecification createRequestSpec(Object requestBody, Map<String, String> headersMap) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);

        if (headersMap != null && !headersMap.isEmpty()) {
            specBuilder.addHeaders(headersMap);
        }

        if (requestBody != null) {
            specBuilder.setBody(requestBody);
        }

        setLogging(specBuilder);
        return specBuilder.build();
    }

    /**
     * CreateRequestSpec based on contentType
     *
     * @param contentType takes content type of type string
     * @return request Specification object
     */
    private RequestSpecification createRequestSpec(String contentType) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);

        setRequestContentType(contentType);
        setLogging(specBuilder);
        return specBuilder.build();
    }

    /**
     * Creates Request Specification based request body and content type and Multiple headers in key value format
     *
     * @param requestBody takes request of object type
     * @param contentType takes content type of String
     * @param headersMap  takes multiple headers in key value format
     * @return Request specification object
     */
    private RequestSpecification createRequestSpec(Object requestBody, String contentType, Map<String, String> headersMap) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);
        setRequestContentType(contentType);
        if (headersMap != null) {
            specBuilder.addHeaders(headersMap);
        }
        if (requestBody != null) {
            specBuilder.setBody(requestBody);
        }
        setLogging(specBuilder);
        return specBuilder.build();
    }

    /**
     * Creates Request Specification based on  Multiple headers in key value format with file and consentRequest as String
     *
     * @param file           takes file as input
     * @param consentRequest takes of type String
     * @param headersMap     takes multiple headers in key value format
     * @return Request specification object
     */
    private RequestSpecification createRequestSpec(Map<String, String> headersMap, File file, String consentRequest) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setBaseUri(baseURI);
        addAuthorizationHeader(specBuilder);
        specBuilder.addMultiPart(ConstantStrings.APP_LOGO.getMessage(),file, Constants.MIME_TYPE_PNG);
        specBuilder.addMultiPart(ConstantStrings.CONSENT_REQUEST.getMessage(), consentRequest);
        if (headersMap != null) {
            specBuilder.addHeaders(headersMap);
        }
        setLogging(specBuilder);
        return specBuilder.build();
    }


    /**
     * Http Method Util for Get call with service url
     *
     * @param serviceUrl takes url in the form of String
     * @return Response object
     */
    public Response get(String serviceUrl) {
        return given(createRequestSpec()).when().get(serviceUrl);
    }

    /**
     * Http Method Util for Post call with Multipart Upload  with file, service url
     *
     * @param serviceUrl     takes url in the form of String
     * @param headersMap     takes headers as map
     * @param file           take file as input
     * @param consentRequest takes as string
     * @return Response object
     */
    public Response postMultiPart(String serviceUrl, Map<String, String> headersMap, File file, String consentRequest) {
        return given(createRequestSpec(headersMap, file, consentRequest)).when().post(serviceUrl);
    }

    /**
     * Http Util for Get call with Service url and headers map
     *
     * @param serviceUrl takes url in the form of String
     * @param headersMap takes headers in form of key and value
     * @return Response object
     */
    public Response get(String serviceUrl, Map<String, String> headersMap) {
        return given(createRequestSpec(headersMap)).when().get(serviceUrl);
    }


    /**
     * Http Util for post call with service url,content type,request body
     *
     * @param serviceUrl  takes url in the form of String
     * @param contentType takes content type in the form of String
     * @param requestBody takes request body in the form of object
     * @return Response object
     */
    public Response post(String serviceUrl, String contentType, Object requestBody) {
        return given(createRequestSpec(requestBody, contentType)).when().post(serviceUrl);
    }

    /**
     *  * Http Util for Get Call with service url and query params in the form of map and headers with map
     * @param serviceUrl takes url in the form of String
     * @param queryParams takes queryParams in form of key and value
     * @param headersMap takes headers in form of key and value
     * @return Response object
     */
    public Response get(String serviceUrl, Map<String, Object> queryParams, Map<String, String> headersMap) {
        return given(createRequestSpec(headersMap, queryParams)).when().get(serviceUrl);
    }

    /**
     * Http Util for post call with service url,headersMap,request body
     *
     * @param serviceUrl  takes url in the form of String
     * @param headersMap  takes headersMap in the form of key and values
     * @param requestBody takes request body in the form of object
     * @return Response object
     */

    public Response post(String serviceUrl, Map<String, String> headersMap, Object requestBody) {
        return given(createRequestSpec(requestBody, headersMap)).when().post(serviceUrl);
    }

    /**
     * Http Util for post call with service url,content type,request body,headers map
     *
     * @param serviceUrl  takes url in the form of String
     * @param contentType takes content type in the form of String
     * @param requestBody takes request body in the form of object
     * @param headersMap  takes header map in the form of key and value
     * @return Response object
     */
    public Response post(String serviceUrl, String contentType, Object requestBody, Map<String, String> headersMap) {
        return given(createRequestSpec(requestBody, contentType, headersMap)).when().post(serviceUrl);
    }

    /**
     * Http Util for post call with service url,headers map
     *
     * @param serviceUrl takes url in the form of String
     * @param headersMap takes header map in the form of key and value
     * @return Response object
     */
    public Response post(String serviceUrl, Map<String, String> headersMap) {
        return given(createRequestSpec(headersMap)).when().post(serviceUrl);
    }

    /**
     * Http Util for post call with service url,content type
     *
     * @param serviceUrl  takes url in the form of String
     * @param contentType takes content type in the form of String
     * @return Response object
     */
    public Response post(String serviceUrl, String contentType) {
        return given(createRequestSpec(contentType)).when().post(serviceUrl);
    }

    /**
     * Http Util for put call with service url,content type,request body
     *
     * @param serviceUrl   takes url in the form of String
     * @param contentType takes content type in the form of String
     * @param requestBody takes request body in the form of object
     * @return Response object
     */
    public Response put(String serviceUrl, String contentType, Object requestBody) {
        return given(createRequestSpec(requestBody, contentType)).when().put(serviceUrl);
    }

    /**
     * Http Util for put call with service url,content type,request body
     *
     * @param serviceUrl takes url in the form of String
     * @param headersMap takes headers in the form of key and value
     * @return Response object
     */
    public Response put(String serviceUrl, Map<String, String> headersMap) {
        return given(createRequestSpec(headersMap)).when().put(serviceUrl);
    }

    /**
     * Http Util for put call with service url,content type,request body,headers map
     *
     * @param serviceUrl  takes url in the form of String
     * @param contentType takes content type in the form of String
     * @param requestBody takes request body in the form of object
     * @param headersMap  takes header map in the form of key and value
     * @return Response object
     */
    public Response put(String serviceUrl, String contentType, Object requestBody, Map<String, String> headersMap) {
        return given(createRequestSpec(requestBody, contentType, headersMap)).when().put(serviceUrl);
    }

    /**
     * Http Util for patch call with service url,content type,request body
     *
     * @param serviceUrl  takes url in the form of String
     * @param contentType takes content type in the form of String
     * @param requestBody takes request body in the form of object
     * @return Response object
     */
    public Response patch(String serviceUrl, String contentType, Object requestBody) {
        return given(createRequestSpec(requestBody, contentType)).when().patch(serviceUrl);
    }

    /**
     * Http Util for patch call with service url,content type,request body,headers map
     *
     * @param serviceUrl  takes url in the form of String
     * @param contentType takes content type in the form of String
     * @param requestBody takes request body in the form of object
     * @param headersMap  takes header map in the form of key and value
     * @return Response object
     */
    public Response patch(String serviceUrl, String contentType, Object requestBody, Map<String, String> headersMap) {
        return given(createRequestSpec(requestBody, contentType, headersMap)).when().patch(serviceUrl);
    }

    /**
     * Http Util for delete call with service url
     *
     * @param serviceUrl serviceUrl takes url in the form of String
     * @return Response object
     */
    public Response delete(String serviceUrl) {
        return given(createRequestSpec()).when().delete(serviceUrl);
    }

    /**
     * Fetch the login token currently all the details are fetched property file . Use only to test the local changes
     * Note : This function is used to fetch the token in local and request data is fetched from Property file
     *
     * @return token as String
     */
    public String getTokenLocal() {
        LoginRequest loginRequest = new LoginRequest(prop.getProperty("email"), prop.getProperty("password"), ConstantStrings.PORTAL.getMessage());
        return given().log().all().contentType(ContentType.JSON).body(loginRequest).when().post(prop.getProperty("tokenUrl")).then().log().all().assertThat().statusCode(HttpStatus.SC_OK).extract().path("token");
    }




}