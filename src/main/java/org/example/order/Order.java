package org.example.order;

public class Order {
    /**
     * Конструктор для состава заказов
     */
    public String[] ingredients;

    public Order(String[] ingredients) {

        this.ingredients = ingredients;
    }

    public String[] getIngredients() {

        return ingredients;
    }

    public void setIngredients(String[] ingredients) {

        this.ingredients = ingredients;
    }
}