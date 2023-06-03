import client.UserClient;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserApiTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken, UserCredentials.from(user));
        }
    }

    @Test
    @DisplayName("Тест на создание пользователя")
    public void userCanBeCreated() {
        userClient.create(user)
                .statusCode(200)
                .body("success", equalTo(true));

        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Тест на создание уже зарегистрированного пользователя")
    public void userAlreadyRegistered() {
        userClient.create(user)
                .statusCode(200)
                .body("success", equalTo(true));

        userClient.create(user)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Тест на создание пользователя без обязательного поля")
    public void userCreateWithoutPassword() {
        user.setPassword(null);
        userClient.create(user)
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
