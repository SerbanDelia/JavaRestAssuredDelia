import io.restassured.RestAssured;
import models.LoginRequest;
import org.testng.annotations.BeforeClass;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {

    @BeforeClass
    public void setup() {
        // Această metodă va rula o singură dată, înainte de orice test din clasa care o moștenește
        System.out.println("Inițializare configurare de bază...");
        RestAssured.baseURI = "https://test.hapifyme.com/api";
    }

    protected String getAuthToken(String userUsername,String userPassword ) {

        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(userUsername);
        loginRequestBody.setPassword(userPassword);

        return RestAssured.given()
                .contentType("application/json")
                .body(loginRequestBody)
                .when()
                .post("/user/login.php")
                .then()
                .assertThat()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().path("token"); // <-- Aici extragem valoarea
    }


}
