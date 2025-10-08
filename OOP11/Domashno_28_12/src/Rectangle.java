class Rectangle extends Shape {
    private double width, height;

    public Rectangle(double x, double y, double width, double height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) other;
            return !(x + width / 2 < rectangle.x - rectangle.width / 2 ||
                    x - width / 2 > rectangle.x + rectangle.width / 2 ||
                    y + height / 2 < rectangle.y - rectangle.height / 2 ||
                    y - height / 2 > rectangle.y + rectangle.height / 2);
        } else if (other instanceof Square) {
            return other.overlaps(this);
        } else if (other instanceof Circle) {
            return other.overlaps(this);
        } else if (other instanceof Trapezoid) {
            return other.overlaps(this);
        }
        return false;
    }
}