import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class QuadTree {
    private double minX, maxX, minY, maxY;
    private List<Shape> shapes;
    private QuadTree[] children;
    private static final int MAX_SHAPES = 10;

    public QuadTree(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.shapes = new ArrayList<>();
        this.children = null;
    }

    public void insert(Shape shape) {
        if (children != null) {
            int index = getChildIndex(shape);
            if (index != -1) {
                children[index].insert(shape);
                return;
            }
        }

        shapes.add(shape);

        if (shapes.size() > MAX_SHAPES && children == null) {
            subdivide();
            redistributeShapes();
        }
    }

    private void subdivide() {
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        children = new QuadTree[4];
        children[0] = new QuadTree(minX, midX, minY, midY);
        children[1] = new QuadTree(midX, maxX, minY, midY);
        children[2] = new QuadTree(minX, midX, midY, maxY);
        children[3] = new QuadTree(midX, maxX, midY, maxY);
    }

    private void redistributeShapes() {
        List<Shape> remainingShapes = new ArrayList<>();
        for (Shape shape : shapes) {
            int index = getChildIndex(shape);
            if (index != -1) {
                children[index].insert(shape);
            } else {
                remainingShapes.add(shape);
            }
        }
        shapes = remainingShapes;
    }

    private int getChildIndex(Shape shape) {
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        boolean bottom = shape.getY() < midY;
        boolean left = shape.getX() < midX;

        if (bottom && left) return 0;
        if (bottom && !left) return 1;
        if (!bottom && left) return 2;
        if (!bottom && !left) return 3;

        return -1;
    }

    public List<AbstractMap.SimpleEntry<Shape, Shape>> findOverlaps() {
        List<AbstractMap.SimpleEntry<Shape, Shape>> overlaps = new ArrayList<>();
        findOverlaps(overlaps, new HashSet<>());
        return overlaps;
    }

    private void findOverlaps(List<AbstractMap.SimpleEntry<Shape, Shape>> overlaps, Set<Shape> checked) {
        for (int i = 0; i < shapes.size(); i++) {
            for (int j = i + 1; j < shapes.size(); j++) {
                if (shapes.get(i).overlaps(shapes.get(j))) {
                    overlaps.add(new AbstractMap.SimpleEntry<>(shapes.get(i), shapes.get(j)));
                }
            }
        }

        if (children != null) {
            for (QuadTree child : children) {
                child.findOverlaps(overlaps, checked);
            }
        }
    }

    public static void printOverlappingShapes(QuadTree quadTree) {
        List<AbstractMap.SimpleEntry<Shape, Shape>> overlaps = quadTree.findOverlaps();
        for (Map.Entry<Shape, Shape> pair : overlaps) {
            Shape shape1 = pair.getKey();
            Shape shape2 = pair.getValue();
            System.out.println(shape1 + " - " + shape2);
        }
    }


    public static QuadTree readFromFile(String filePath) throws IOException {
        QuadTree quadTree = new QuadTree(-1000, 1000, -1000, 1000);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                String type = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);

                Shape shape;
                switch (type.toLowerCase()) {
                    case "square":
                        double side = Double.parseDouble(parts[3]);
                        shape = new Square(x, y, side);
                        break;
                    case "rectangle":
                        double width = Double.parseDouble(parts[3]);
                        double height = Double.parseDouble(parts[4]);
                        shape = new Rectangle(x, y, width, height);
                        break;
                    case "circle":
                        double radius = Double.parseDouble(parts[3]);
                        shape = new Circle(x, y, radius);
                        break;
                    case "trapezoid":
                        double base1 = Double.parseDouble(parts[3]);
                        double base2 = Double.parseDouble(parts[4]);
                        double heightTrap = Double.parseDouble(parts[5]);
                        shape = new Trapezoid(x, y, base1, base2, heightTrap);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown shape type: " + type);
                }

                quadTree.insert(shape);
            }
        }

        return quadTree;
    }

}
