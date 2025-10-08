class Trapezoid extends Shape {
    private double base1, base2, height;

    public Trapezoid(double x, double y, double base1, double base2, double height) {
        super(x, y);
        this.base1 = base1;
        this.base2 = base2;
        this.height = height;
    }

    public double getBase1() {
        return base1;
    }

    public double getBase2() {
        return base2;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other instanceof Square || other instanceof Rectangle || other instanceof Circle || other instanceof Trapezoid) {
            double minX = x - Math.max(base1, base2) / 2;
            double maxX = x + Math.max(base1, base2) / 2;
            double minY = y - height / 2;
            double maxY = y + height / 2;

            if (other instanceof Square) {
                Square square = (Square) other;
                return !(square.x + square.getSide() / 2 < minX ||
                        square.x - square.getSide() / 2 > maxX ||
                        square.y + square.getSide() / 2 < minY ||
                        square.y - square.getSide() / 2 > maxY);
            }
            if (other instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) other;
                return !(rectangle.x + rectangle.getWidth() / 2 < minX ||
                        rectangle.x - rectangle.getWidth() / 2 > maxX ||
                        rectangle.y + rectangle.getHeight() / 2 < minY ||
                        rectangle.y - rectangle.getHeight() / 2 > maxY);
            }
            if (other instanceof Circle) {
                Circle circle = (Circle) other;
                double closestX = Math.max(minX, Math.min(circle.x, maxX));
                double closestY = Math.max(minY, Math.min(circle.y, maxY));
                double distance = Math.hypot(circle.x - closestX, circle.y - closestY);
                return distance <= circle.getRadius();
            }
            if (other instanceof Trapezoid) {
                Trapezoid trapezoid = (Trapezoid) other;
                double otherMinX = trapezoid.x - Math.max(trapezoid.base1, trapezoid.base2) / 2;
                double otherMaxX = trapezoid.x + Math.max(trapezoid.base1, trapezoid.base2) / 2;
                double otherMinY = trapezoid.y - trapezoid.height / 2;
                double otherMaxY = trapezoid.y + trapezoid.height / 2;
                return !(otherMaxX < minX || otherMinX > maxX || otherMaxY < minY || otherMinY > maxY);
            }
        }
        return false;
    }
}