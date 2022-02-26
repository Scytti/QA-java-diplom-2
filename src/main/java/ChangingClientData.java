import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ChangingClientData {
    private static final String USER_PATH = "/auth/user";

    @Step
    public ValidatableResponse changeDataWithAuth(Object object, String accessUserToken){
        return given()
                    .header("Authorization", accessUserToken)
                    .spec(Base.getBaseSpec())
                    .body(object)
                    .when()
                    .patch(USER_PATH)
                    .then();
    }

    @Step
    public ValidatableResponse changeDataWithoutAuth(Object object){
        return given()
                    .spec(Base.getBaseSpec())
                    .body(object)
                    .when()
                    .patch(USER_PATH)
                    .then();
    }
}
