public class Cook implements Runnable {
    public final String name;
    private final Restaurant restaurant;

    public Cook(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
    }

    private void log(String msg) {
        System.out.println("[Cook " + name + "] " + msg);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = restaurant.takeNextOrder();

                if (order == null) {
                    synchronized (restaurant.getLock()) {
                        restaurant.getLock().wait();
                    }
                    continue;
                }

                log("Започва поръчка #" + order.getId());

                boolean bakingNeeded = false;

                for (String ing : order.getIngredients()) {
                    log("Добавя: " + ing);
                    if (ing.toLowerCase().contains("хляб")) {
                        Thread.sleep(1500);
                    } else {
                        Thread.sleep(1200);
                    }

                    if ((ing.toLowerCase().contains("кашкавал") || ing.toLowerCase().contains("топено"))
                            && order.needsBaking()) {
                        bakingNeeded = true;
                        try {
                            restaurant.sendToOven(order);
                        } catch (InterruptedException e) {
                            log("Грешка при изпращане във фурната: " + e.getMessage());
                            Thread.currentThread().interrupt();
                            return;
                        }
                        break; // спира добавянето на други съставки, сандвичът отива във фурната
                    }
                }

                if (!bakingNeeded && !restaurant.isOrderFinalized(order.getId())) {
                    try {
                        restaurant.finalizeOrder(order);
                        log("Завърши поръчка #" + order.getId());
                    } catch (Exception e) {
                        log("Грешка при финализиране на поръчка #" + order.getId() + ": " + e.getMessage());
                    }
                }
            }
        } catch (InterruptedException ignored) {
            // Thread was interrupted - exit gracefully
        }
    }
}