import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class OrderTest {

    @Test
    public void testValidClassicHam() {
        Order order = Order.classicHam("бял хляб");
        assertTrue(order.isValid(), "Classic Ham should be valid");
    }

    @Test
    public void testInvalidNoBread() {
        List<String> ingredients = List.of("шунка", "кашкавал", "домат", "лук", "майонеза");
        Order order = new Order(ingredients, List.of());
        assertFalse(order.isValid(), "Order without bread should be invalid");
    }

    @Test
    public void testValidWithNoBake() {
        Order order = new Order(
                List.of("пълнозърнест хляб", "кашкавал", "домати", "чушка", "сос чеснов"),
                List.of("no bake")
        );
        assertTrue(order.isValid(), "Valid order with 'no bake' request should pass");
        assertFalse(order.needsBaking(), "Order with 'no bake' should not require baking");
    }

    @Test
    public void testTooManySauces() {
        Order order = new Order(
                List.of("хляб", "кашкавал", "домат", "лук", "сос 1", "сос 2", "сос 3", "сос 4"),
                List.of()
        );
        assertFalse(order.isValid(), "Order with more than 3 sauces should be invalid");
    }

    @Test
    public void testTooFewVeggies() {
        Order order = new Order(
                List.of("хляб", "кашкавал", "сос чеснов"),
                List.of()
        );
        assertFalse(order.isValid(), "Order with fewer than 1 veggie should be invalid");
    }

    @Test
    public void testFactoryVeggieDelight() {
        Order order = Order.veggieDelight("пълнозърнест хляб");
        assertTrue(order.isValid(), "Veggie Delight factory method should return valid order");
    }
}
