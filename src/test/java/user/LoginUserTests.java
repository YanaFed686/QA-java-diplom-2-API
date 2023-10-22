package user;

import io.qameta.allure.junit4.DisplayName;
import org.example.user.User;
import org.example.user.UserRequests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class LoginUserTests {

    private final User user = User.createRandomUser();
    private final String loginErrorMessage = "email or password are incorrect";

    @Before
    public void setUp() {

        UserRequests.createUser(user);
    }

    @After
    public void tearDown(){
        UserRequests.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void loginUser(){
        var loginResponse = UserRequests.login(new User(user.getEmail(), user.getPassword(), user.getName()));
        boolean isResponseSuccess = loginResponse.extract().path("success");
        int actualStatusCode = loginResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    public void loginUserWithWrongEmail(){
        var loginResponse = UserRequests.login(new User("BlahBlahBlah123@yondex.ru", user.getPassword(), user.getName()));
        boolean isResponseSuccess = loginResponse.extract().path("success");
        String responseMessage = loginResponse.extract().path("message");
        int actualStatusCode = loginResponse.extract().statusCode();
        //Проверяем, что изменение невозможно, ошибка 401, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_UNAUTHORIZED);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, loginErrorMessage);
    }


    @Test
    @DisplayName("Авторизация с неверным паролем.")
    public void loginUserWithWrongPassword(){
        var loginResponse = UserRequests.login(new User(user.getEmail(), "OneMoreForgottenPassword", user.getName()));
        boolean isResponseSuccess = loginResponse.extract().path("success");
        String responseMessage = loginResponse.extract().path("message");
        int actualStatusCode = loginResponse.extract().statusCode();
        //Проверяем, что изменение невозможно, ошибка 401, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_UNAUTHORIZED);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, loginErrorMessage);
    }
}