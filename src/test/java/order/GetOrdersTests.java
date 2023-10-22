package order;

import io.qameta.allure.junit4.DisplayName;
import org.example.order.Ingredients;
import org.example.order.OrderRequests;
import org.example.user.User;
import org.example.user.UserRequests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetOrdersTests {


    private final User user = User.createRandomUser();
    List<String> ingredients = new ArrayList<>();
    public String orderIngredients;
    String accessToken;
    Ingredients createOrder;

    //выбор рандомного ингредиента из 15ти возможных
    public int randomNumber(){
        Random random = new Random();
        int maxbound = 15;
        return random.nextInt(maxbound);
    }

    @Before
    public void setUp() {
        ingredients = OrderRequests.getIngredients().extract().path("data._id");
        orderIngredients = ingredients.get(randomNumber());
        accessToken = UserRequests.createUser(user).extract().path("accessToken");
        createOrder = new Ingredients(orderIngredients);
    }

    @After
    public void tearDown(){
        UserRequests.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Получить список заказов, когда пользователь авторизован")
    public void getOrdersWithUserLogin(){
        var getListOrders = OrderRequests.getOrders(accessToken);

        int actualStatusCode = getListOrders.extract().statusCode();
        boolean isResponseSuccess = getListOrders.extract().path("success");

        Assert.assertEquals("Order list is not received", actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
    }

    @Test
    @DisplayName("Получить список заказов, когда пользователь неавторизован")
    public void getOrdersWithoutLogin(){
        var getOrdersList = OrderRequests.getOrders("");
        String errorMessage = "You should be authorised";

        int actualStatusCode = getOrdersList.extract().statusCode();
        boolean isResponseSuccess = getOrdersList.extract().path("success");
        String responseMessage = getOrdersList.extract().path("message");

        //Проверяем, что на наш запрос вернулась ошибка 401 и сообщение "You should be authorised"
        Assert.assertEquals(actualStatusCode, SC_UNAUTHORIZED);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, errorMessage);
    }
}