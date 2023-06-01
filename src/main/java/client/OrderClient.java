package client;

import io.restassured.response.ValidatableResponse;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String ORDERS_PATH = "api/orders";

    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    public ValidatableResponse getOrdersWithAuthorization(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .when()
                .get(ORDERS_PATH)
                .then();
    }

    public ValidatableResponse getOrdersWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH)
                .then();
    }
}
