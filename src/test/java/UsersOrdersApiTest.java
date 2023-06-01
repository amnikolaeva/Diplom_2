import client.OrderClient;
import client.UserClient;
import generator.UserGenerator;
import io.restassured.RestAssured;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UsersOrdersApiTest {

    private UserClient userClient;
    private User user;
    private UserCredentials userCredentials;
    private String accessToken;
    private OrderClient orderClient;

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
