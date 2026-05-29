package pl.malgorzata.galera.tests;

import pl.malgorzata.galera.base.BaseTest;
import pl.malgorzata.galera.models.Customer;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CustomerRegistrationTests extends BaseTest {

    @Test
    public void shouldRegisterNewCustomerAndReturnCompleteProfile() {
        Customer registrationPayload = Customer.builder()
                .firstName("Krystyna")
                .lastName("Lewandowska")
                .email("krystyna.lewandowska@example-bank.pl")
                .pesel("93060345440")
                .password("q4v4tz0hA1!")
                .build();

        Customer registeredCustomer = given()
                .spec(requestSpec)
                .body(registrationPayload)
                .when()
                .post(CUSTOMER_ENDPOINT)
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract()
                .as(Customer.class);

        assertThat("Customer ID should be generated", registeredCustomer.getId(), notNullValue());
        assertThat("First name mismatch", registeredCustomer.getFirstName(), equalTo(registrationPayload.getFirstName()));
    }
}
