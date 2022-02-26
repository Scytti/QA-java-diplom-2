import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetOrderTest {

    UserClient userClient;
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

        ingredients.add(idOfIngredients.get(1));
        ingredients.add(idOfIngredients.get(2));
        ingredients.add(idOfIngredients.get(3));
        orderHash.put("ingredients", idOfIngredients);
        orderClient.createOrder(orderHash,accessToken);
    }

    @Test
    public void getOrdersAuthUserTest(){
        ValidatableResponse userOrder = orderClient.getOrderWithAuth(accessToken);

        assertEquals("Status is not 200!",200,userOrder.extract().statusCode());
        assertEquals("Order get unsuccessful", true, userOrder.extract().path("success"));
    }

    @Test
    public void getOrdersWithNoAuthUserTest(){
        ValidatableResponse userOrder = orderClient.getOrderWithoutAuth();

        assertEquals("Status is not 401!",401,userOrder.extract().statusCode());
        assertEquals("You should be authorised", userOrder.extract().path("message"));
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
