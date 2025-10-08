import java.util.concurrent.atomic.AtomicInteger;

abstract class Shape {
    protected double x, y;
    protected String name;
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    public Shape(double x, double y) {
        this.x = x;
        this.y = y;
        this.name = getClass().getSimpleName().toLowerCase() + idGenerator.getAndIncrement();
    }

    public String getName() {
        return name;
    }

    public abstract boolean overlaps(Shape other);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("%s(%.1f, %.1f)", name, x, y);
    }
}