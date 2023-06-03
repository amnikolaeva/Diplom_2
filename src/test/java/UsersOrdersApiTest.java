import client.OrderClient;
import client.UserClient;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UsersOrdersApiTest {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private OrderClient orderClient;

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken, UserCredentials.from(user));
        }
    }

    @Test
    @DisplayName("Тест на получение заказов неавторизованного пользователя")
    public void getUsersOrdersWithoutAuthorization() {
        orderClient = new OrderClient();
        orderClient.getOrdersWithoutAuthorization()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Тест на получение заказов авторизованного пользователя")
    public void getUsersOrdersWithAuthorization() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user))
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
        orderClient = new OrderClient();
        orderClient.getOrdersWithAuthorization(accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }
}
