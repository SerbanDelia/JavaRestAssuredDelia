import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class HealthCheckTest extends BaseTest {

    @Test
    public void systemIsHealthyTest() {
//        // Given - (precondiții, setări inițiale)
//        RestAssured.baseURI = "https://test.hapifyme.com/api";

        // When - (acțiunea pe care o testăm)
        RestAssured.when()
                .get("/system/health_check.php")

                // Then - (validarea rezultatului)
                .then()
                .assertThat()
                .statusCode(200);
    }

        @Test
        public void testS1() {
            RestAssured.when()
                    .get("/system/health_check.php")

                            .then()
                            .assertThat()
                            .statusCode(401)
                            .body("status", equalTo("error"))
                            .body("message", equalTo( "API key is required."));
        }
    }
