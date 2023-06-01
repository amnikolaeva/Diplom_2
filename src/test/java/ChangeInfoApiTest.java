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

public class ChangeInfoApiTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        userClient.create(user);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken, UserCredentials.from(user));
        }
    }

    @Test
    public void changeEmailWithAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setEmail("email124789237504932562043875");
        userClient.changeInfo(accessToken, UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void changePasswordWithAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setPassword("password124789237504932562043875");
        userClient.changeInfo(accessToken, UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void changeNameWithAuthorization() {
        accessToken = userClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");

        user.setName("name124789237504932562043875");
        userClient.changeInfo(accessToken, UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
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
