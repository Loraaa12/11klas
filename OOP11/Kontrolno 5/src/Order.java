import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static AtomicInteger counter = new AtomicInteger(1);
    private final int id;
    private final List<String> ingredients;
    private final List<String> specialRequests;

    public Order(List<String> ingredients, List<String> specialRequests) {
        this.id = counter.getAndIncrement();
        this.ingredients = new ArrayList<>(ingredients);
        this.specialRequests = new ArrayList<>(specialRequests);
    }

    public int getId() {
        return id;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getSpecialRequests() {
        return specialRequests;
    }

    public boolean isValid() {
        boolean hasBread = ingredients.stream().anyMatch(i -> i.toLowerCase().contains("хляб"));
        boolean hasCheese = ingredients.stream().anyMatch(i -> i.toLowerCase().contains("кашкавал") || i.toLowerCase().contains("топено"));
        long sauces = ingredients.stream().filter(i -> i.toLowerCase().contains("сос")).count();
        long veggies = ingredients.stream().filter(i -> List.of("домат", "чушка", "маслини", "лук", "краставица", "айсберг").contains(i.toLowerCase())).count();

        return hasBread && hasCheese && sauces >= 1 && sauces <= 3 && veggies >= 1 && veggies <= 3;
    }

    public boolean needsBaking() {
        return !specialRequests.contains("no bake");
    }

    @Override
    public String toString() {
        return "Order #" + id + " | Съставки: " + ingredients + " | Изисквания: " + specialRequests;
    }

    public static Order classicHam(String bread) {
        return new Order(List.of(bread, "шунка", "кашкавал", "домат", "лук", "краставица", "майонеза"), List.of());
    }

    public static Order longBurger(String bread) {
        return new Order(List.of(bread, "телешко", "топено сирене", "айсберг", "кисели краставички", "барбекю сос"), List.of());
    }

    public static Order veggieDelight(String bread) {
        return new Order(List.of(bread, "кашкавал", "айсберг", "маслини", "домати", "сос ранч"), List.of());
    }
}
