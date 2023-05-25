import client.UserClient;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import user.User;

public class UserCreateTest {

    @Test
    public void userCanBeCreated() {
        User user = new User("test@test.ru", "password", "name");
        UserClient userClient = new UserClient();

        ValidatableResponse userCreateResponse = userClient.userCreate();
        int statusCode = userCreateResponse.extract().statusCode();
        boolean isUserCreate = userCreateResponse.extract().path("success");
    }
}
