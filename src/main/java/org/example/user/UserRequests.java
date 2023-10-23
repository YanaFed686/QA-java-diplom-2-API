package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.ListOfApiHandlers;

import static io.restassured.RestAssured.given;

public class UserRequests extends ListOfApiHandlers {

    /**
     * Данные для создания, аутентификации, изменения данных, получения токена и удаления клиента.
     * Использованы 'log().all() для отображения подробной информации в процессе выполнения теста
     */

    @Step("Создание клиента")
    public static ValidatableResponse createUser(User user){
        return given()
                .spec(baseRequest()).log().all()
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Аутентификация клиента")
    public static ValidatableResponse login(User user){
        return given()
                .spec(baseRequest()).log().all()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Изменение данных клиента")
    public static ValidatableResponse changeUserData(String token, User user){
        return given()
                .header("Authorization", token)
                .spec(baseRequest()).log().all()
                .when()
                .body(user)
                .patch(GET_USER_DATA)
                .then();
    }

    @Step("Получение токена")
    public static String getToken(User user){

        return login(user).extract().path("accessToken");
    }

    @Step("Удаление клиента по токену")
    public static void deleteUserByToken(String token){
        given()
                .header("Authorization", token)
                .spec(baseRequest()).log().all()
                .when()
                .delete(GET_USER_DATA)
                .then()
                .assertThat().statusCode(202);
    }
    /**
     * Используется в методе 'tearDown'
     */
    @Step("Удаление клиента по имени")
    public static void deleteUserByUser(User user) {
        if (getToken(user) != null) {
            deleteUserByToken(getToken(user));
        }
    }
}
