import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {

    public UserClient userClient;
    private String accessToken;

    @Before
    public void before(){
        userClient = new UserClient();
    }

    @Test
    public void createUserTest(){
        User user = User.getRandom();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 200",200, statusCode);
    }

    @Test
    public void createUserWithoutNameTest(){
        User user = User.getRandom();
        user.setName("");
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Status is not 403",403, statusCode);
        assertEquals("Email, password and name are required fields", message);
    }

    @Test
    public void createAlreadyExistUserTest(){
        User user =User.getRandom();
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Status is not 403",403, statusCode);
        assertEquals("Incorrect message","User already exists", message);
    }

    @After
    public void after(){
        if (accessToken != null){
            ValidatableResponse response = userClient.deleteUser(accessToken);
            int statusCode = response.extract().statusCode();
            assertEquals(202,statusCode);
        }
    }
}
