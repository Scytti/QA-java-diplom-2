import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    private static UserClient userClient;
    String accessToken;

    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;

    @Before
    public void before(){
        User user = User.getRandom();
        userClient = new UserClient();
        accessToken = userClient.createUser(user).extract().path("accessToken");

        orderHash = new HashMap<>();
        orderClient = new OrderClient();
        List <String> idOfIngredients = orderClient.getIngredients().extract().path("data._id");
        for (int i = 1; i <= 5; i = i + 1) {
            ingredients.add(idOfIngredients.get(i+5));
        }
        orderHash.put("ingredients", idOfIngredients);
    }

    @Test
    public void createOrderWithIngredientsAuth(){

        ValidatableResponse createOrder = orderClient.createOrder(orderHash, accessToken);
        assertEquals("Status is not 200",200, createOrder.extract().statusCode());
        assertEquals("Incorrect success field",true, createOrder.extract().path("success"));

    }

    @Test
    public void createOrderWithoutAuth(){

        ValidatableResponse createOrder = orderClient.createOrder(orderHash, "");
        assertEquals("Status is not 200",200, createOrder.extract().statusCode());
        assertEquals("Incorrect success field",true, createOrder.extract().path("success"));
    }

    @Test
    public void createOrderWithoutIngredients(){
        HashMap<String, List> nullOrderHash = new HashMap<>();
        ValidatableResponse createOrder = orderClient.createOrder(nullOrderHash, accessToken);
        assertEquals("Status is not 400",400, createOrder.extract().statusCode());
        assertEquals("Incorrect message","Ingredient ids must be provided", createOrder.extract().path("message"));
    }

    @Test
    public void createOrderWithIncorrectHash(){
        String invalidHashIngredients = RandomStringUtils.randomAlphanumeric(5,15);
        List<String> invalidIngredients = new ArrayList<>();
        HashMap<String, List> invalidOrderHash = new HashMap<>();
        invalidIngredients.add(invalidHashIngredients);
        invalidOrderHash.put("ingredients",invalidIngredients);

        ValidatableResponse createOrder = orderClient.createOrder(invalidOrderHash, accessToken);
        assertEquals("Status is not 500",500, createOrder.extract().statusCode());
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
