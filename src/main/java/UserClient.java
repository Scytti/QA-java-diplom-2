import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {
    public static final String REGISTER_URL = "/auth/register";
    public static final String LOGIN_URL = "/auth/login";

    @Step
    public ValidatableResponse createUser(User user){
        return given()
                .spec(Base.getBaseSpec())
                .and()
                .body(user)
                .when()
                .post(REGISTER_URL)
                .then();
    }

    @Step
    public ValidatableResponse loginUser(UserCredentials userCredentials){
        return given()
           .spec(Base.getBaseSpec())
                .and()
                .body(userCredentials)
                .when()
                .post(LOGIN_URL)
                .then();
    }

    @Step
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(Base.getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("auth/user")
                .then();
    }
}
