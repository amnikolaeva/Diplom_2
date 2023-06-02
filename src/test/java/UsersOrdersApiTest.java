import client.OrderClient;
import client.UserClient;
import generator.OrderGenerator;
import generator.UserGenerator;
import io.restassured.RestAssured;
import model.Order;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UsersOrdersApiTest {

    private UserClient userClient;
    private User user;
    private UserCredentials userCredentials;
    private String accessToken;
    private OrderClient orderClient;
    private Order order;
    private List<String> ingredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user))
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken, UserCredentials.from(user));
        }
    }

     @Test
     public void createOrderWithAuthorization() {
        order = new Order(ingredients);
        order.getIngredients();
        orderClient = new OrderClient();
        order = OrderGenerator.getOrder();
        user = UserGenerator.getRandom();
        userClient = new UserClient();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user))
                 .extract().path("accessToken");
        orderClient.createOrderWithAuthorization(accessToken, order)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order", notNullValue());
     }

    @Test
    public void createOrderWithoutAuthorization() {
        order = new Order(ingredients);
        orderClient = new OrderClient();
        order = OrderGenerator.getOrder();
        user = UserGenerator.getRandom();
        userClient = new UserClient();
        userClient.create(user);
        orderClient.createOrderWithoutAuthorization(order)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order", notNullValue());
    }

    @Test
    public void createOrderWithoutIngredients() {
        order = new Order(ingredients);
        order.setIngredients(null);
        orderClient = new OrderClient();
        orderClient.createOrderWithoutAuthorization(order)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithIncorrectIngredients() {
        order = new Order(ingredients);
        order = OrderGenerator.getOrderWithIncorrectIngredients();
        orderClient = new OrderClient();
        orderClient.createOrderWithoutAuthorization(order)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("jwt expired"));
    }

    @Test
    public void getUsersOrdersWithAuthorization() {
        orderClient = new OrderClient();
        orderClient.getOrdersWithAuthorization(accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    public void getUsersOrdersWithoutAuthorization() {
        orderClient = new OrderClient();
        orderClient.getOrdersWithoutAuthorization()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
