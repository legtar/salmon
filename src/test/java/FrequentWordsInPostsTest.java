import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.given;

public class FrequentWordsInPostsTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeAll
    public static void initialize() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    void shouldIdentifyTop10FrequentWordsInPosts() {
        Map<String, Integer> wordOccurrences = new HashMap<>();

        // Iterate over all posts
        for (int i = 1; i <= 100; i++) {
            String postBody = fetchPostBodyById(i);
            updateWordFrequencyMap(wordOccurrences, postBody);
        }

        // Display top 10 frequent words
        wordOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }

    private String fetchPostBodyById(int postId) {
        Response response = given()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}");

        return response.jsonPath().getString("body");
    }

    private void updateWordFrequencyMap(Map<String, Integer> wordFrequency, String body) {
        Arrays.stream(body.split("\\W+"))
                .map(String::toLowerCase)
                .forEach(word -> wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1));
    }
}
