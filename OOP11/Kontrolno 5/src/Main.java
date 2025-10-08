import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Restaurant restaurant = new Restaurant();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- WaySub Меню ---");
            System.out.println("1. Добави готвач");
            System.out.println("2. Създай нова поръчка");
            System.out.println("3. Изход");

            switch (scanner.nextLine()) {
                case "1" -> addCook();
                case "2" -> createOrder();
                case "3" -> {
                    restaurant.shutdown();
                    return;
                }
            }
        }
    }

    private static void addCook() {
        System.out.print("Име на готвача: ");
        String name = scanner.nextLine();
        try {
            restaurant.addCook(new Cook(name, restaurant));
        } catch (Exception e) {
            System.out.println("Грешка: " + e.getMessage());
        }
    }

    private static void createOrder() {
        System.out.println("1. Classic Ham\n2. Long Burger\n3. Veggie Delight\n4. Свободна поръчка");
        String choice = scanner.nextLine();
        System.out.print("Въведи вид хляб: ");
        String bread = scanner.nextLine();

        Order order = switch (choice) {
            case "1" -> Order.classicHam(bread);
            case "2" -> Order.longBurger(bread);
            case "3" -> Order.veggieDelight(bread);
            default -> {
                System.out.print("Съставки (разделени със запетая): ");
                List<String> ingredients = List.of(scanner.nextLine().split(",\\s*"));
                System.out.print("Специални изисквания (разделени със запетая): ");
                List<String> special = List.of(scanner.nextLine().split(",\\s*"));
                yield new Order(ingredients, special);
            }
        };

        if (!order.isValid()) {
            System.out.println("Невалидна поръчка!");
            return;
        }
        restaurant.addOrder(order);
    }
}
