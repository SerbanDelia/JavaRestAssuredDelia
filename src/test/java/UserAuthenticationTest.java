import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.LoginRequest; // <-- Importăm clasa POJO
import org.hamcrest.Matcher;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*; // <-- Import esențial pentru matchers!

public class UserAuthenticationTest extends BaseTest {

    @Test
    public void loginWithInvalidCredentialsFails() {
        // 1. Creăm obiectul Java (POJO) care modelează body-ul
        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername("email_invalid");
        loginRequestBody.setPassword("parola_gresita");

        // Given
        RestAssured.given()
                .contentType("application/json") // Specificăm că trimitem JSON
                .body(loginRequestBody) // <-- Aici se întâmplă magia!
                // When
                .when()
                .post("/user/login.php")   //pana aici tine de request
                // Then
                .then()
                .assertThat()
                .statusCode(401) // Ne așteptăm la 401 Unauthorized  // pana aici tine de raspuns
                .body("message", equalTo("Invalid username or password"));

        System.out.println("Intradevar, testul a trecut!");
    }



    @Test
        public void loginWithValidCredentialsSucceeds() {
            // Presupunem că acest utilizator există în baza de date de test
            String userUsername = "jane232_doe2123";
            String userPassword = "qazXSW@13";

            LoginRequest loginRequestBody = new LoginRequest();
            loginRequestBody.setUsername(userUsername);
            loginRequestBody.setPassword(userPassword);


            String authToken = RestAssured.given() // Am declarat variabila authToken
                    .contentType("application/json")
                    .body(loginRequestBody)
                    .when()
                    .post("/user/login.php")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    // Validăm conținutul body-ului
                    .body("message", equalTo("Login successful"))
                    .body("token", notNullValue()) // Verificăm că token-ul există
                    .body("user.username", equalTo(userUsername)) // Verificăm că datele sunt corecte
                    .body("token", notNullValue())
                    .extract().path("token"); // <-- Aici extragem valoarea

        // Acum putem folosi variabila 'authToken'
        System.out.println("Token extras: " + authToken);

    }

}
