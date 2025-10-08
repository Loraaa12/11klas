class Circle extends Shape {
    private double radius;

    public Circle(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other instanceof Circle) {
            Circle circle = (Circle) other;
            double distance = Math.hypot(x - circle.x, y - circle.y);
            return distance <= radius + circle.radius;
        } else if (other instanceof Square) {
            return other.overlaps(this);
        } else if (other instanceof Rectangle) {
            return other.overlaps(this);
        } else if (other instanceof Trapezoid) {
            return other.overlaps(this);
        }
        return false;
    }
}
