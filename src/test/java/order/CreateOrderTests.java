package order;

import io.qameta.allure.junit4.DisplayName;
import org.example.order.Ingredients;
import org.example.order.OrderRequests;
import org.example.user.User;
import org.example.user.UserRequests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTests {


    private final User user = User.createRandomUser();
    List<String> ingredients = new ArrayList<>();

//выбор рандомного ингредиента из 15ти возможных
    public int randomNumber(){
        Random random = new Random();
        int maxbound = 15;
        return random.nextInt(maxbound);
    }

    @After
    public void tearDown(){
        UserRequests.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    public void createOrder(){
        String accessToken = UserRequests.createUser(user).extract().path("accessToken");
        ingredients = OrderRequests.getIngredients().extract().path("data._id");
        Ingredients numberIngredients = new Ingredients(ingredients.get(randomNumber()));
        var createOrder = OrderRequests.createOrder(numberIngredients, accessToken);

        //получаем и проверяем код ответа при успешном создании заказа и получаем номер заказа
        int getStatusCode = createOrder.extract().statusCode();
        boolean isResponseSuccess = createOrder.extract().path("success");
        int orderNumber = createOrder.extract().path("order.number");

        Assert.assertEquals(getStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertNotNull(orderNumber);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но без ингредиентов")
    public void createOrderWithoutIngredients(){
        String accessToken = UserRequests.createUser(user).extract().path("accessToken");
        Ingredients orderIngredients = new Ingredients("");
        var createOrder = OrderRequests.createOrder(orderIngredients, accessToken);
        String ingredientsErrorMessage = "Ingredient ids must be provided";

        //получаем и проверяем код ответа при неверном запросе на создание заказа
        int actualStatusCode = createOrder.extract().statusCode();
        boolean isResponseSuccess = createOrder.extract().path("success");
        String responseMessage = createOrder.extract().path("message");

        Assert.assertEquals(actualStatusCode, SC_BAD_REQUEST);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, ingredientsErrorMessage);
    }

    @Test
    @DisplayName("Создание заказа без авторизации, но с ингредиентами")
    public void createOrderWithoutLogin(){
        ingredients = OrderRequests.getIngredients().extract().path("data._id");
        Ingredients orderIngredients = new Ingredients(ingredients.get(randomNumber()));
        var createOrder = OrderRequests.createOrder(orderIngredients, "");

        //получаем и проверяем код ответа при успешном создании заказа и получаем номер заказа
        int actualStatusCode = createOrder.extract().statusCode();
        boolean isResponseSuccess = createOrder.extract().path("success");
        int orderNumber = createOrder.extract().path("order.number");

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertNotNull(orderNumber);
    }

    @Test
    @DisplayName("Попытка создания заказа с неверными ингредиентами")
    public void createOrderInvalidIngredients(){
        String accessToken = UserRequests.createUser(user).extract().path("accessToken");
        Ingredients invalidIngredients = new Ingredients("Mandalorian sandwich");
        var createOrder = OrderRequests.createOrder(invalidIngredients, accessToken);

        //получаем и проверяем код ответа и ошибку при создании заказа
        int actualStatusCode = createOrder.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_INTERNAL_SERVER_ERROR);
    }
}