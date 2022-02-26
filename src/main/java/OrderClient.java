import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public static final String ORDER_URL = "/orders";
    public static final String INGREDIENTS_URL = "/ingredients";

    @Step
    public ValidatableResponse getIngredients (){
        return given()
                .spec(Base.getBaseSpec())
                .when()
                .get(INGREDIENTS_URL)
                .then();
    }

    @Step
    public ValidatableResponse createOrder (HashMap ingredients, String accessToken){
        return given()
                .header("Authorization", accessToken)
                .spec(Base.getBaseSpec())
                .and()
                .body(ingredients)
                .when()
                .post(ORDER_URL)
                .then();
    }

    @Step
    public ValidatableResponse getOrderWithAuth (String accessToken){
        return given()
                .header("Authorization", accessToken)
                .spec(Base.getBaseSpec())
                .when()
                .get(ORDER_URL)
                .then();
    }

    @Step
    public ValidatableResponse getOrderWithoutAuth (){
        return given()
                .spec(Base.getBaseSpec())
                .when()
                .get(ORDER_URL)
                .then();
    }
}
