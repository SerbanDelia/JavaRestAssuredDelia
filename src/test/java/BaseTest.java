import io.restassured.RestAssured;
import models.LoginRequest;
import org.testng.annotations.BeforeClass;
import utils.ConfigManager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {

    @BeforeClass
    public void setup() {
        // Această metodă va rula o singură dată, înainte de orice test din clasa care o moștenește
        System.out.println("Inițializare configurare de bază...");
        RestAssured.baseURI = ConfigManager.getProperty("base.url");
    }

    protected String getAuthToken(String username, String userPassword) {

        LoginRequest loginRequestBody = new LoginRequest();
        loginRequestBody.setUsername(username);
        loginRequestBody.setPassword(userPassword);

        return RestAssured.given()
                .contentType("application/json")
                .body(loginRequestBody)
                .when()
                .post("/user/login.php")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json")
                // Validăm conținutul body-ului
                .body("token", notNullValue()) // Verificăm că token-ul există
                .extract().path("token"); // <-- Aici extragem valoarea
    }

    protected void deleteUser(String username, String password) {
// Pas 1: Obține token-ul pentru utilizatorul pe care vrem să-l ștergem
        LoginRequest loginBody = new LoginRequest();
        loginBody.setUsername(username);
        loginBody.setPassword(password);
        String token;
        try {
            token = RestAssured.given()
                    .contentType("application/json")
                    .body(loginBody)
                    .when().post("/user/login.php")
                    .then().extract().path("token");
            System.out.println("Utilizatorul cu username-ul " + username + " si parola ... a fost sters cu succes!" );
        } catch (Exception e) {
            // Dacă login-ul eșuează (ex: userul nu a fost creat), nu facem nimic
            System.out.println("Nu am putut șterge utilizatorul, probabil nu a fost creat.");
            return;
        }

        // Pas 2: Trimite request-ul de ștergere autentificat
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/user/delete_profile.php")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("User profile deleted successfully."));
    }
}