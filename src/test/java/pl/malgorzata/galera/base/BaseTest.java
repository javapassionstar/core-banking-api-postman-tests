package pl.malgorzata.galera.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail; // Add this import
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;
import pl.malgorzata.galera.config.ConfigReader;

public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    public static final String CUSTOMER_ENDPOINT = "/api/v1/customers";
    public static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    public static final String ACCOUNT_ENDPOINT = "/api/v1/accounts";
    public static final String ACCOUNT_BALANCE_ENDPOINT = "/api/v1/accounts/{accountNumber}/balance";


    @BeforeSuite
    public void globalSetup() {
        RestAssured.baseURI = ConfigReader.getBaseUri();
        RestAssured.port = ConfigReader.getBasePort();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
}
