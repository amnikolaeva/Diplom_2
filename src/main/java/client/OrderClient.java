package client;

import io.restassured.response.ValidatableResponse;
import model.User;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDERS_PATH = "api/orders";

    public ValidatableResponse create() {
        return given()
                .spec(getBaseSpec())
                .when()
                .post(ORDERS_PATH)
                .then();
    }
}
