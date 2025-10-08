import java.util.concurrent.*;
import java.util.*;

public class Oven implements Runnable {
    private final BlockingQueue<Order> bakingQueue = new ArrayBlockingQueue<>(1);
    private final Queue<Order> bakedOrders = new ConcurrentLinkedQueue<>();

    public void addOrder(Order order) throws InterruptedException {
        bakingQueue.put(order);
    }

    public Order takeBakedOrder() {
        return bakedOrders.poll();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = bakingQueue.take();
                System.out.println("[Oven] Пече поръчка #" + order.getId());
                Thread.sleep(2500);
                bakedOrders.offer(order);
                synchronized (this) {
                    this.notifyAll();
                }
                System.out.println("[Oven] Изпечена поръчка #" + order.getId());
            }
        } catch (InterruptedException ignored) {}
    }
}