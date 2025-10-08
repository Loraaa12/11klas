import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

class QuadTreeTest {

    @Test
    void testSquareOverlap() {
        Square square1 = new Square(0, 0, 4);
        Square square2 = new Square(2, 2, 4);
        Square square3 = new Square(6, 6, 2);

        assertTrue(square1.overlaps(square2));
        assertFalse(square1.overlaps(square3));
    }

    @Test
    void testCircleOverlap() {
        Circle circle1 = new Circle(0, 0, 3);
        Circle circle2 = new Circle(2, 2, 2);
        Circle circle3 = new Circle(10, 10, 1);

        assertTrue(circle1.overlaps(circle2));
        assertFalse(circle1.overlaps(circle3));
    }

    @Test
    void testQuadTreeInsertion() {
        QuadTree quadTree = new QuadTree(-1000, 1000, -1000, 1000);
        Square square = new Square(0, 0, 4);
        Circle circle = new Circle(500, 500, 50);

        quadTree.insert(square);
        quadTree.insert(circle);

        assertEquals(2, quadTree.findOverlaps().size());
    }

    @Test
    void testFileReadingAndOverlaps() throws IOException {
        QuadTree quadTree = QuadTree.readFromFile("test_shapes.txt");

        List<AbstractMap.SimpleEntry<Shape, Shape>> overlaps = quadTree.findOverlaps();
        assertNotNull(overlaps);
        assertTrue(overlaps.size() > 0);

        // Example expected overlap
        Map.Entry<Shape, Shape> exampleOverlap = overlaps.get(0);
        assertNotNull(exampleOverlap.getKey());
        assertNotNull(exampleOverlap.getValue());
    }

//    @Test
//    public void testQuadTreeSplitting() {
//        QuadTree quadTree = new QuadTree(-1000, 1000, -1000, 1000);
//
//        for (int i = 0; i < 12; i++) {
//            quadTree.insert(new Square(i * 5, i * 5, 2)); // Add 12 squares
//        }
//
//        assertEquals(4, quadTree.getChildIndex().size()); // Ensure root has 4 children
//    }

//    @Test
//    public void testFindOverlapsWithinNode() {
//        QuadTree quadTree = new QuadTree(-1000, 1000, -1000, 1000);
//
//        Square square1 = new Square(10, 10, 5);
//        Square square2 = new Square(12, 12, 4); // Overlaps with square1
//        Circle circle = new Circle(50, 50, 10); // Does not overlap
//
//        quadTree.insert(square1);
//        quadTree.insert(square2);
//        quadTree.insert(circle);
//
//        List<Pair<Shape, Shape>> overlaps = quadTree.findOverlaps();
//
//        assertEquals(1, overlaps.size());
//        assertTrue(overlaps.contains(new Pair<>(square1, square2)));
//    }

    @Test
    public void testSquareOverlapsSquare() {
        Square square1 = new Square(5, 5, 4); // Center (5,5), side length 4
        Square square2 = new Square(6, 6, 4); // Overlapping square

        assertTrue(square1.overlaps(square2));
        assertTrue(square2.overlaps(square1));
    }

    @Test
    public void testSquareDoesNotOverlapSquare() {
        Square square1 = new Square(5, 5, 4); // Center (5,5), side length 4
        Square square2 = new Square(15, 15, 4); // Non-overlapping square

        assertFalse(square1.overlaps(square2));
        assertFalse(square2.overlaps(square1));
    }

    @Test
    public void testSquareTouchingSquareAtBoundary() {
        Square square1 = new Square(5, 5, 4); // Center (5,5), side length 4
        Square square2 = new Square(9, 5, 4); // Touching square at right boundary

        assertTrue(square1.overlaps(square2));
        assertTrue(square2.overlaps(square1));
    }

    @Test
    public void testCircleOverlapsCircle() {
        Circle circle1 = new Circle(10, 10, 5); // Center (10,10), radius 5
        Circle circle2 = new Circle(13, 13, 5); // Overlapping circle

        assertTrue(circle1.overlaps(circle2));
        assertTrue(circle2.overlaps(circle1));
    }

    @Test
    public void testCircleDoesNotOverlapCircle() {
        Circle circle1 = new Circle(10, 10, 5); // Center (10,10), radius 5
        Circle circle2 = new Circle(30, 30, 5); // Non-overlapping circle

        assertFalse(circle1.overlaps(circle2));
        assertFalse(circle2.overlaps(circle1));
    }

    @Test
    public void testCircleTouchingCircleAtBoundary() {
        Circle circle1 = new Circle(0, 0, 5); // Center (0,0), radius 5
        Circle circle2 = new Circle(10, 0, 5); // Touching circle at right boundary

        assertTrue(circle1.overlaps(circle2));
        assertTrue(circle2.overlaps(circle1));
    }

    @Test
    public void testTrapezoidOverlapsRectangle() {
        Trapezoid trapezoid = new Trapezoid(5, 5, 6, 4, 3); // Center (5,5), bases 6 and 4, height 3
        Rectangle rectangle = new Rectangle(6, 6, 5, 4); // Overlapping rectangle

        assertTrue(trapezoid.overlaps(rectangle));
        assertTrue(rectangle.overlaps(trapezoid));
    }

    @Test
    public void testTrapezoidDoesNotOverlapRectangle() {
        Trapezoid trapezoid = new Trapezoid(5, 5, 6, 4, 3); // Center (5,5), bases 6 and 4, height 3
        Rectangle rectangle = new Rectangle(20, 20, 5, 4); // Non-overlapping rectangle

        assertFalse(trapezoid.overlaps(rectangle));
        assertFalse(rectangle.overlaps(trapezoid));
    }

    @Test
    public void testTrapezoidTouchingRectangleAtBoundary() {
        Trapezoid trapezoid = new Trapezoid(5, 5, 6, 4, 3); // Center (5,5), bases 6 and 4, height 3
        Rectangle rectangle = new Rectangle(9, 5, 4, 2); // Touching rectangle at right boundary

        assertTrue(trapezoid.overlaps(rectangle));
        assertTrue(rectangle.overlaps(trapezoid));
    }

    @Test
    public void testSquareTouchingCircleAtBoundary() {
        Square square = new Square(5, 5, 4); // Center (5,5), side length 4
        Circle circle = new Circle(9, 5, 2); // Touching circle at right boundary

        assertTrue(square.overlaps(circle));
        assertTrue(circle.overlaps(square));
    }

    @Test
    public void testRectangleTouchingRectangleAtBoundary() {
        Rectangle rectangle1 = new Rectangle(5, 5, 6, 4); // Center (5,5), sides 6x4
        Rectangle rectangle2 = new Rectangle(11, 5, 6, 4); // Touching rectangle at right boundary

        assertTrue(rectangle1.overlaps(rectangle2));
        assertTrue(rectangle2.overlaps(rectangle1));
    }

    @Test
    public void testTrapezoidTouchingCircleAtBoundary() {
        Trapezoid trapezoid = new Trapezoid(5, 5, 6, 4, 3); // Center (5,5), bases 6 and 4, height 3
        Circle circle = new Circle(11, 5, 2); // Touching circle at right boundary

        assertTrue(trapezoid.overlaps(circle));
        assertTrue(circle.overlaps(trapezoid));
    }






}
