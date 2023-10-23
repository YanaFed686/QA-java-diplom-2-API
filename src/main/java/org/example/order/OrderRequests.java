package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.ListOfApiHandlers;

import static io.restassured.RestAssured.given;

public class OrderRequests extends ListOfApiHandlers {

    /**
     * Данные для создания и получения заказов и ингредиентов.
     * Использованы 'log().all() для отображения подробной информации в процессе выполнения теста
     */
    @Step("Создание заказа с передачей ингредиентов и токена")
    public static ValidatableResponse createOrder(Ingredients ingredients, String token){
        return given()
                .headers("Authorization", token)
                .spec(baseRequest()).log().all()
                .body(ingredients)
                .when()
                .post(CREATE_ORDER)
                .then();
    }

    @Step("Получение данных о заказах клиента")
    public static ValidatableResponse getOrders(String token){
        return given()
                .header("Authorization", token)
                .spec(baseRequest()).log().all()
                .get(CREATE_ORDER)
                .then();
    }

    @Step("Получение ингредиентов")
    public static ValidatableResponse getIngredients(){
        return given()
                .spec(baseRequest()).log().all()
                .when()
                .get(GET_INGREDIENTS)
                .then();
    }
}