package generator;

import model.Order;

import java.util.List;

public class OrderGenerator {

    public static Order getOrder() {
        List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72");
        return new Order(ingredients);
    }

    public static Order getOrderWithIncorrectIngredients() {
        List<String> ingredients = List.of("6a71d1f82001bdaaa6d", "61c0c5f82001bdaaa72");
        return new Order(ingredients);
    }
}
