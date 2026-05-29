package pl.malgorzata.galera.tests;

import pl.malgorzata.galera.base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AccountBalanceTests extends BaseTest {

    @Test
    public void shouldFetchAccountBalanceSuccessfully() {
        String dynamicAccountNumber = given()
                .spec(requestSpec)
                .when()
                .get(ACCOUNT_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("accountNumber");

        given()
                .spec(requestSpec)
                .pathParam("accountNumber", dynamicAccountNumber)
                .when()
                .get(ACCOUNT_BALANCE_ENDPOINT)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("balance", notNullValue());
    }
}
