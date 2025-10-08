import java.util.*;
import java.util.concurrent.*;

public class Restaurant {
    private final Map<String, Thread> cookThreads = new ConcurrentHashMap<>();
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final Set<Integer> completedOrders = ConcurrentHashMap.newKeySet();
    private final Oven oven = new Oven();
    private final Thread ovenThread = new Thread(oven);
    private final Object lock = new Object();

    public Restaurant() {
        ovenThread.start();
    }

    public void addCook(Cook cook) throws Exception {
        if (cookThreads.containsKey(cook.name)) throw new Exception("Готвач с това име съществува");
        Thread t = new Thread(cook);
        cookThreads.put(cook.name, t);
        t.start();
    }

    public void addOrder(Order order) {
        orderQueue.offer(order);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public boolean isOrderFinalized(int id) {
        return completedOrders.contains(id);
    }

    public void finalizeOrder(Order order) throws Exception {
        if (completedOrders.contains(order.getId()))
            throw new Exception("Поръчката вече е завършена");
        completedOrders.add(order.getId());
        System.out.println("[Restaurant] Поръчка #" + order.getId() + " е завършена!");
    }

    public Order takeNextOrder() {
        Order baked = oven.takeBakedOrder();
        if (baked != null && !completedOrders.contains(baked.getId())) return baked;
        return orderQueue.poll();
    }

    public void sendToOven(Order order) throws InterruptedException {
        oven.addOrder(order);
    }

    public Object getLock() {
        return lock;
    }

    public void shutdown() {
        cookThreads.values().forEach(Thread::interrupt);
        ovenThread.interrupt();
    }
}