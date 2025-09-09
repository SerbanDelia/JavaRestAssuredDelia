import io.restassured.RestAssured;
import models.LoginRequest; // <-- Importăm clasa POJO
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAuthenticationTest extends BaseTest {

    @DataProvider(name = "invalidLoginCredentialsProvider")
    public Object[][] invalidLoginCredentialsProvider() {
        return new Object[][] {
                { "john1757442014378_doe1757442014378", "password123", "Invalid username or password" },
                { "test.username", "5Zm7%hECD{@[", "Invalid username or password" },
                { "", "", "Invalid username or password" }
        };
    }

    @Test(dataProvider = "invalidLoginCredentialsProvider")
    public void loginWithInvalidCredentialsFails(String username, String password, String expectedErrorMessage) {
        // 1. Creăm obiectul Java (POJO) care modelează body-ul
        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(username);
        loginRequestBody.setPassword(password);

        // Given
        RestAssured.given()
                .contentType("application/json") // Specificăm că trimitem JSON
                .body(loginRequestBody) // <-- Aici se întâmplă magia!
                // When
                .when()
                .post("/user/login.php")
                // Then
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo(expectedErrorMessage)); // Ne așteptăm la 401 Unauthorized
        System.out.println("Intr-adevar, testul a trecut. Logarea cu username " + "<<" + username + ">>" + " si parola " + "<<" + password +">>" + " a esuat, asa cum ne asteptam.");
    }
    //    {
//        "username": "jane232_doe2123",
//            "password": "qazXSW@13"
//    }
    @Test
    public void loginWithValidCredentialsSucceeds() {
        // Presupunem că acest utilizator există în baza de date de test
        String username = "jane232_doe2123";
        String userPassword = "qazXSW@13";

        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(username);
        loginRequestBody.setPassword(userPassword);

        String authToken = RestAssured.given()
                .contentType("application/json")
                .body(loginRequestBody)
                .when()
                .post("/user/login.php")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json")
                // Validăm conținutul body-ului
                .body("message", equalTo("Login successful"))
                .body("user.username", equalTo(username)) // Verificăm că datele sunt corecte
                .body("token", notNullValue()) // Verificăm că token-ul există
                .extract().path("token"); // <-- Aici extragem valoarea

        System.out.println("Token extras: " + authToken);
    }

}