import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginUserTest {
    UserClient userClient;
    String accessToken;
    String email;
    String password;
    String expectedMessage = "email or password are incorrect";

    @Before
    public void before(){
        userClient = new UserClient();
        User user = User.getRandom();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        email = response.extract().path("user.email");
        password = user.getPassword();
        assertEquals("Status is not 200",200, statusCode);
    }

    @Test
    public void correctLoginUserTest(){
        ValidatableResponse response = userClient.loginUser(new UserCredentials(email, password));
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 200",200, statusCode);
    }

    @Test
    public void loginUserWithIncorrectPasswordTest(){
        ValidatableResponse response = userClient.loginUser(new UserCredentials(email, password+"123"));
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 401",401, statusCode);
        String message = response.extract().path("message");
        assertEquals("Incorrect message",expectedMessage, message);
    }

    @Test
    public void loginUserWithIncorrectEmailTest(){
        ValidatableResponse response = userClient.loginUser(new UserCredentials(email+"654", password));
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 401",401, statusCode);
        String message = response.extract().path("message");
        assertEquals("Incorrect message",expectedMessage, message);
    }

    @Test
    public void loginUserWithIncorrectEmailAndPasswordTest(){
        ValidatableResponse response = userClient.loginUser(new UserCredentials(email+"654", password+"987"));
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 401",401, statusCode);
        String message = response.extract().path("message");
        assertEquals("Incorrect message",expectedMessage, message);
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
