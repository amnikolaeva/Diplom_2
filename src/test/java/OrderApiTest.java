import client.OrderClient;
import client.UserClient;
import generator.OrderGenerator;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import model.User;
import model.UserCredentials;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderApiTest {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private OrderClient orderClient;
    private Order order;

    @Test
    @DisplayName("Тест на создание заказа с авторизацией")
    public void createOrderWithAuthorization() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        order = OrderGenerator.getOrder();
        user = UserGenerator.getRandom();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user))
                .extract().path("accessToken");
        orderClient.createOrderWithAuthorization(accessToken, order)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order", notNullValue());
        userClient.delete(accessToken, UserCredentials.from(user));
    }

    @Test
    @DisplayName("Тест на создание заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        order = OrderGenerator.getOrder();
        orderClient.createOrderWithoutAuthorization(order)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Тест на создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        order = new Order(null);
        orderClient = new OrderClient();
        orderClient.createOrderWithoutAuthorization(order)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Тест на создание заказа с некорректными ингредиентами ")
    public void createOrderWithIncorrectIngredients() {
        order = OrderGenerator.getOrderWithIncorrectIngredients();
        orderClient = new OrderClient();
        orderClient.createOrderWithoutAuthorization(order)
                .statusCode(500);
    }
}
