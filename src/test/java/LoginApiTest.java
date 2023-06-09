import client.UserClient;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginApiTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    private static final String INCORRECT_PASSWORD = "1";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken, UserCredentials.from(user));
        }
    }

    @Test
    @DisplayName("Тест на авторизацию пользователя")
    public void userCanBeLogin() {
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Тест на авторизацию пользователя с некорректным паролем")
    public void userLoginWithIncorrectPassword() {
        user.setPassword(INCORRECT_PASSWORD);
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .extract().path("accessToken");
    }
}
