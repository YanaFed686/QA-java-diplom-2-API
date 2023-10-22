package user;

import io.qameta.allure.junit4.DisplayName;
import org.example.user.UserRequests;
import org.junit.After;
import org.junit.Assert;
import org.example.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class ChangeUserDataTests {

    private final User user = User.createRandomUser();
    private final String unauthorisedErrorMessage = "You should be authorised";
    private final String emailErrorMessage = "User with such email already exists";


    @Before
    public void setUp() {

        UserRequests.createUser(user);
    }

    @After
    public void tearDown() {
        UserRequests.deleteUserByUser(user);
    }

    @Test
    @DisplayName("Изменение email у авторизованного пользователя")
    public void changeEmail() {
        String newEmail = User.createRandomEmail();
        User user = new User(newEmail, this.user.getPassword(), this.user.getName());
        var changeResponse = UserRequests.changeUserData(UserRequests.getToken(this.user), user);
        boolean isResponseSuccess = changeResponse.extract().path("success");
        String changedEmail = changeResponse.extract().path("user.email");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertEquals(changedEmail, newEmail.toLowerCase());
    }

    @Test
    @DisplayName("Изменение пароля у авторизованного пользователя")
    public void changePassword() {
        String newPassword = User.createRandomUserData();
        User newUser = new User(user.getEmail(), newPassword, user.getName());
        var changeResponse = UserRequests.changeUserData(UserRequests.getToken(user), newUser);

        boolean isResponseSuccess = changeResponse.extract().path("success");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
    }

    @Test
    @DisplayName("Изменение имени у авторизованного пользователя")
    public void changeName() {
        String newUserName = User.createRandomUserData();
        User changedUser = new User(user.getEmail(), user.getPassword(), newUserName);
        var changeResponse = UserRequests.changeUserData(UserRequests.getToken(user), changedUser);

        boolean isResponseSuccess = changeResponse.extract().path("success");
        String actualUserName = changeResponse.extract().path("user.name");
        int actualStatusCode = changeResponse.extract().statusCode();

        Assert.assertEquals(actualStatusCode, SC_OK);
        Assert.assertTrue(isResponseSuccess);
        Assert.assertEquals(actualUserName, newUserName);
    }

    @Test
    @DisplayName("Изменение данных у неавторизованного пользователя")
    public void changeDataWithoutLogin() {
        var changeResponse = UserRequests.changeUserData("", user);
        boolean isResponseSuccess = changeResponse.extract().path("success");
        String responseMessage = changeResponse.extract().path("message");
        int actualStatusCode = changeResponse.extract().statusCode();
        //Проверяем, что изменение невозможно, ошибка 401, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_UNAUTHORIZED);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, unauthorisedErrorMessage);

    }

    @Test
    @DisplayName("Попытка изменить email на уже существующий")
    public void changeUserEmailToExistedEmail() {

        //Создаем первого пользователя
        User firstUser = User.createRandomUser();
        UserRequests.createUser(firstUser);
        String firstUserEmail = firstUser.getEmail();
        //Создаем второго пользователя с таким же email
        User secondUser = new User(firstUserEmail, user.getPassword(), user.getName());
        //Отправляем запрос на изменение данных email на уже существующий
        var changeResponse = UserRequests.changeUserData(UserRequests.getToken(user), secondUser);
        boolean isResponseSuccess = changeResponse.extract().path("success");
        String responseMessage = changeResponse.extract().path("message");
        int actualStatusCode = changeResponse.extract().statusCode();

        //Проверяем, что изменение невозможно, ошибка 403, присутствует сообщение об ошибке
        Assert.assertEquals(actualStatusCode, SC_FORBIDDEN);
        Assert.assertFalse(isResponseSuccess);
        Assert.assertEquals(responseMessage, emailErrorMessage);
    }
}