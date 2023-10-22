package user;

import io.qameta.allure.junit4.DisplayName;
import org.example.user.User;
import org.example.user.UserRequests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class CreateUserTests {

    private final User user = User.createRandomUser();
    private final String emptyFieldErrorMessage = "Email, password and name are required fields";
    private final String existedUserErrorMessage = "User already exists";

    @After
    public void tearDown(){
        UserRequests.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUser(){
        var createResponse = UserRequests.createUser(new User(user.getEmail(), user.getPassword(), user.getName()));
        boolean isResponseSuccess = createResponse.extract().path("success");
        var actualStatusCode = createResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя по уже существующим параметрам")
    public void createUserWithExistedData(){
        UserRequests.createUser(new User("yankafed@ya.ru", user.getPassword(), user.getName()));
        var createResponse = UserRequests.createUser(new User("yankafed@ya.ru", user.getPassword(), user.getName()));
        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        var actualStatusCode = createResponse.extract().statusCode();

        //Проверяем, что изменение невозможно, ошибка 403, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, existedUserErrorMessage);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем email")
    public void createUserWithoutEmail(){
        var createResponse = UserRequests.createUser(new User("", user.getPassword(), user.getName()));
        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        var actualStatusCode = createResponse.extract().statusCode();

        //Проверяем, что изменение невозможно, ошибка 403, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, emptyFieldErrorMessage);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем password")
    public void createUserWithoutPassword(){
        var createResponse = UserRequests.createUser(new User(user.getEmail(), "", user.getName()));
        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        int actualStatusCode = createResponse.extract().statusCode();

        //Проверяем, что изменение невозможно, ошибка 403, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, emptyFieldErrorMessage);

    }

    @Test
    @DisplayName("Создание пользователя с пустым полем name")
    public void createUserWithoutName(){
        var createResponse =  UserRequests.createUser(new User(user.getEmail(), user.getPassword(),""));
        boolean isResponseSuccess = createResponse.extract().path("success");
        String responseMessage = createResponse.extract().path("message");
        int actualStatusCode = createResponse.extract().statusCode();

        //Проверяем, что изменение невозможно, ошибка 403, присутствует сообщение об ошибке
        Assert.assertEquals("User was created with empty name", actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, emptyFieldErrorMessage);
    }
}