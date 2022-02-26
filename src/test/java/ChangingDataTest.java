import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangingDataTest {

    ChangingClientData changingClientData;
    private static UserClient userClient;
    User user = User.getRandom();
    String accessToken;
    User expectedData;
    ValidatableResponse createdUser;
    String emailMessage = "Email after changing doesnt match";
    String nameMessage = "Name after changing doesnt match";

    @Before
    public void before(){
        changingClientData = new ChangingClientData();
        userClient = new UserClient();
        createdUser = userClient.createUser(user);
        accessToken = createdUser.extract().path("accessToken");
    }

    @Test
    public void changeEmailDataWithAuthTest(){
        expectedData = new User(user.getAnotherEmail(), user.getPassword(), user.getName());
        ValidatableResponse changedData = changingClientData.changeDataWithAuth(expectedData,accessToken);

        assertEquals("Status is not 200",200, changedData.extract().statusCode());
        assertEquals(emailMessage,expectedData.email,changedData.extract().path("user.email"));
        assertEquals(nameMessage, expectedData.name,changedData.extract().path("user.name"));
    }

    @Test
    public void changeEmailOnEqualEmailDataWithAuthTest(){
        User secondUser = User.getRandom();
        createdUser = userClient.createUser(secondUser);

        expectedData = new User(secondUser.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse changedData = changingClientData.changeDataWithAuth(expectedData,accessToken);

        assertEquals("Status is not 403",403, changedData.extract().statusCode());
        assertEquals("Incorrect message","User with such email already exists",changedData.extract().path("message"));
    }

    @Test
    public void changePasswordDataWithAuthTest(){
        expectedData = new User(user.getEmail(), user.getAnotherPassword(), user.getEmail());
        changingClientData.changeDataWithAuth(expectedData,accessToken);

        ValidatableResponse response = userClient.loginUser(new UserCredentials(expectedData.getEmail(), expectedData.getPassword()));
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 200",200, statusCode);
    }

    @Test
    public void changeNameDataWithAuthTest(){
        expectedData = new User(user.getEmail(), user.getPassword(), user.getAnotherName());
        ValidatableResponse changedData = changingClientData.changeDataWithAuth(expectedData,accessToken);

        assertEquals("Status is not 200",200, changedData.extract().statusCode());
        assertEquals(emailMessage,expectedData.email,changedData.extract().path("user.email"));
        assertEquals(nameMessage, expectedData.name,changedData.extract().path("user.name"));
    }

    @Test
    public void changeAllDataWithAuthTest(){
        expectedData = new User(user.getAnotherEmail(), user.getAnotherPassword(), user.getAnotherEmail());
        ValidatableResponse changedData = changingClientData.changeDataWithAuth(expectedData,accessToken);

        assertEquals(emailMessage,expectedData.email,changedData.extract().path("user.email"));
        assertEquals(nameMessage, expectedData.name,changedData.extract().path("user.name"));

        ValidatableResponse response = userClient.loginUser(new UserCredentials(expectedData.getEmail(), expectedData.getPassword()));
        int statusCode = response.extract().statusCode();
        assertEquals("Status is not 200",200, statusCode);
    }

    @Test
    public void changeEmailDataWithoutAuthTest(){
        expectedData = new User(user.getAnotherEmail(), user.getPassword(), user.getName());
        ValidatableResponse editData = changingClientData.changeDataWithoutAuth(expectedData);

        int statusCode = editData.extract().statusCode();
        String message = editData.extract().path("message");

        assertEquals("Incorrect status code",401, statusCode);
        assertEquals("Incorrect message about auth","You should be authorised", message);
    }

    @Test
    public void changePasswordDataWithoutAuthTest(){
        expectedData = new User(user.getEmail(), user.getAnotherPassword(), user.getEmail());
        ValidatableResponse editData = changingClientData.changeDataWithoutAuth(expectedData);

        int statusCode = editData.extract().statusCode();
        String message = editData.extract().path("message");

        assertEquals("Incorrect status code",401, statusCode);
        assertEquals("Incorrect message about auth","You should be authorised", message);

    }

    @Test
    public void changeNameDataWithoutAuthTest(){
        expectedData = new User(user.getEmail(), user.getPassword(), user.getAnotherName());
        ValidatableResponse editData = changingClientData.changeDataWithoutAuth(expectedData);

        int statusCode = editData.extract().statusCode();
        String message = editData.extract().path("message");

        assertEquals("Incorrect status code",401, statusCode);
        assertEquals("Incorrect message about auth","You should be authorised", message);
    }

    @Test
    public void changeAllDataWithoutAuthTest(){
        expectedData = new User(user.getAnotherEmail(), user.getAnotherPassword(), user.getAnotherName());
        ValidatableResponse editData = changingClientData.changeDataWithoutAuth(expectedData);

        int statusCode = editData.extract().statusCode();
        String message = editData.extract().path("message");

        assertEquals("Incorrect status code",401, statusCode);
        assertEquals("Incorrect message about auth","You should be authorised", message);
    }

    @After
    public void after() {
        if (accessToken != null) {
            ValidatableResponse response = userClient.deleteUser(accessToken);
            int statusCode = response.extract().statusCode();
            assertEquals("User is not deleted",202, statusCode);
        }
    }
}
