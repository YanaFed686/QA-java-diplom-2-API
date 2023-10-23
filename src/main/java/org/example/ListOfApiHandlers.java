package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class ListOfApiHandlers {

    /**
     * перечислены все ручки из документации, так как проект может разиваться, тесты будут дописываться
     */
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String GET_INGREDIENTS = "/api/ingredients";
    public static final String CREATE_ORDER = "/api/orders";
    public static final String RECOVERY_PASSWORD = "/api/password-reset";
    public static final String RESET_PASSWORD = "/api/password-reset/reset";
    public static final String CREATE_USER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";
    public static final String LOGOUT = "/api/auth/logout";
    public static final String REFRESH_TOKEN = "/api/auth/token";
    public static final String GET_USER_DATA = "/api/auth/user";
    public static final String DELETE_USER = "/api/auth/user";
    public static final String GET_ALL_ORDERS = "/api/orders/all";

    protected static RequestSpecification baseRequest(){
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}