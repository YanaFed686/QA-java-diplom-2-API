package org.example.user;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    /**
     * Конструктор для создания клиента
     */
    private  String email;
    private  String password;
    private  String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {

        return email;
    }

    public String getPassword() {

        return password;
    }

    public String getName() {

        return name;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setName(String name) {

        this.name = name;
    }

    /**
     * Создание рандомного тестового клиента с использованием RandomString для полей имени, пароля и почт.адреса
     */
    public static User createRandomUser(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@ya.ru".toLowerCase();
        final String password = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        return new User(email, password, name);
    }

    public static String createRandomEmail(){

        /**
         * Создание рандомного тестового email с использованием RandomString
         */

        return RandomStringUtils.randomAlphabetic(10)+"@ya.ru".toLowerCase();
    }
    public static String createRandomUserData(){

        /**
         * Создание рандомной текстовой строки с использованием RandomString для теста на изменение данных клиента
         */

        return RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }
}