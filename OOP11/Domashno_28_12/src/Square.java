class Square extends Shape {
    private double side;

    public Square(double x, double y, double side) {
        super(x, y);
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other instanceof Square) {
            Square square = (Square) other;
            return !(x + side / 2 < square.x - square.side / 2 ||
                    x - side / 2 > square.x + square.side / 2 ||
                    y + side / 2 < square.y - square.side / 2 ||
                    y - side / 2 > square.y + square.side / 2);
        } else if (other instanceof Rectangle) {
            return other.overlaps(this);
        } else if (other instanceof Circle) {
            return other.overlaps(this);
        } else if (other instanceof Trapezoid) {
            return other.overlaps(this);
        }
        return false;
    }
}