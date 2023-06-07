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

public class ChangeInfoApiTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    private static final String CHANGED_EMAIL = "email124789237504932562043875";
    private static final String CHANGED_PASSWORD = "password124789237504932562043875";
    private static final String CHANGED_NAME = "name124789237504932562043875";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken, UserCredentials.from(user));
        }
    }

    @Test
    @DisplayName("Тест на изменение email с авторизацией")
    public void changeEmailWithAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setEmail(CHANGED_EMAIL);
        userClient.changeInfo(accessToken, UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Тест на изменение пароля с авторизацией")
    public void changePasswordWithAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setPassword(CHANGED_PASSWORD);
        userClient.changeInfo(accessToken, UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Тест на изменение имени с авторизацией")
    public void changeNameWithAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setName(CHANGED_NAME);
        userClient.changeInfo(accessToken, UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Тест на изменение email без авторизации ")
    public void changeEmailWithoutAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setEmail("email124789237504932562043875");
        userClient.changeInfo("", UserCredentials.from(user))
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
