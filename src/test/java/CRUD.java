import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CRUD {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeAll
    public static void configureRestAssured() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    void shouldCreateNewPostSuccessfully() {
        String newPostPayload = """
            {
                "title": "A New Post",
                "body": "This is a test post body",
                "userId": 1
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(newPostPayload)
                .when()
                .post("/posts")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("title", is("A New Post"))
                .body("body", is("This is a test post body"))
                .body("userId", is(1));
    }

    @Test
    void shouldRetrieveExistingPost() {
        int postId = 1;

        Response response = given()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}");

        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("id", is(postId))
                .body("title", not(emptyOrNullString()))
                .body("body", not(emptyOrNullString()))
                .body("userId", greaterThan(0));
    }

    @Test
    void shouldUpdatePostDetails() {
        int postId = 1;
        String updatedPostPayload = """
            {
                "title": "Modified Post Title",
                "body": "Modified post body content",
                "userId": 1
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updatedPostPayload)
                .pathParam("id", postId)
                .when()
                .put("/posts/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("title", is("Modified Post Title"))
                .body("body", is("Modified post body content"))
                .body("userId", is(1));
    }

    @Test
    void shouldDeletePostSuccessfully() {
        int postId = 1;

        given()
                .pathParam("id", postId)
                .when()
                .delete("/posts/{id}")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
