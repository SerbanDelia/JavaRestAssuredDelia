import io.restassured.RestAssured;
import models.RegisterRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.ConfigManager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegistrationTest extends BaseTest {

    private String userEmail = "user123_delia@lala.com";
    private String userPassword = "qazXSW@13";
    private String username = "popescu_ion";



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
        registerRequestBody.setEmail(ConfigManager.getProperty("user.username"));
        registerRequestBody.setPassword(ConfigManager.getProperty("user.password"));


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

    @AfterMethod
    public void cleanup() {
        // Curățăm utilizatorul creat în test
        if (ConfigManager.getProperty("user.username") != null) {
            deleteUser(ConfigManager.getProperty("user.username"),ConfigManager.getProperty ("user.password"));

        }

    }
}
