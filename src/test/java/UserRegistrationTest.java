import com.fasterxml.jackson.databind.ser.Serializers;
import io.restassured.RestAssured;
import models.RegisterRequest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegistrationTest extends BaseTest {

    @Test
    public void registerWithMismatchedPasswordsFails() {
        RegisterRequest registerRequestBody = new RegisterRequest();
        registerRequestBody.setFirstName("Delia");
        registerRequestBody.setLastName("Serban");
        registerRequestBody.setEmail("Delia@lala.com");
        registerRequestBody.setPassword("qazXSW@13");


        RestAssured.given()
                .contentType("application/json")
                .body(registerRequestBody)
                .when()
                .post("/user/register.php")
                .then()
                .assertThat()
                .statusCode(400);

    }

    @Test
    public void registerNewUserSucceeds() {
        RegisterRequest registerRequestBody = new RegisterRequest();
        registerRequestBody.setFirstName("Popescu");
        registerRequestBody.setLastName("Ion");
        registerRequestBody.setEmail("user_delia@lala.com");
        registerRequestBody.setPassword("qazXSW@13");


        RestAssured.given()
                .contentType("application/json")
                .body(registerRequestBody)
                .when()
                .post("/user/register.php")
                .then()
                .assertThat()
                .statusCode(201)
               .body("status", equalTo("success"))
               .body("user_id", notNullValue());

          }
}
