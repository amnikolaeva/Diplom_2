package client;

import io.restassured.response.ValidatableResponse;
import user.User;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    private static final String USER_PATH = "api/auth/register";

    public ValidatableResponse userCreate(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH)
                .then();
    }
}
