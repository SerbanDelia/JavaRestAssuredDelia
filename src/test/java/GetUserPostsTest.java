import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class GetUserPostsTest extends BaseTest {

    @Test
    public void getUserPostWithValidTokenReturnsUserData() {
        // 1. Obținem token-ul
        String token = getAuthToken ("jane232_doe2123", "qazXSW@13");

        // 2. Facem request-ul către endpoint-ul securizat
        RestAssured.given()
                .header("Authorization", "Bearer " + token) // <-- Aici folosim token-ul!
                .when()
                .get("/user/get_user_posts.php")
                .then()
                .assertThat()
                .statusCode(200)
                .body("status", notNullValue()) // Validăm că primim datele profilului
                .body("status", equalTo("success"));
    }

    @Test
    public void getUserPostWithoutTokenFails() {
        // Test negativ: încercăm să accesăm resursa FĂRĂ token
        RestAssured.given()
                // Nu adăugăm header-ul de autorizare
                .when()
                .get("/user/get_user_posts.php")
                .then()
                .assertThat()
                .statusCode(401); // Ne așteptăm la eroare de neautorizat
    }
}

