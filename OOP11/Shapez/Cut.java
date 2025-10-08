package Shapez;

import java.util.Arrays;
import java.util.List;

public class Cut extends Machine {
    private boolean isHorizontal;
    private Shape temp;
    private Shape new1;
    private Shape new2;
    private List<String> keys = Arrays.asList("ur", "ul", "lr", "ll");

    public void push(Shape shape, boolean direction) {
        temp = shape;
        this.isHorizontal = direction;
        new1 = new Shape();
        new2 = new Shape();

        System.out.println("Cut push shape with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + temp.get(key)); 
        }
    }

    @Override
    public Shape pull() {
        if (isHorizontal) {
            new1.put("ur", temp.getOrDefault("ur", null));
            new1.put("ul", temp.getOrDefault("ul", null));
            new1.put("lr", null);
            new1.put("ll", null);

            new2.put("ur", null);
            new2.put("ul", null);
            new2.put("lr", temp.getOrDefault("lr", null));
            new2.put("ll", temp.getOrDefault("ll", null));
        } else {
            new1.put("ur", temp.getOrDefault("ur", null));
            new1.put("ul", null);
            new1.put("lr", temp.getOrDefault("lr", null));
            new1.put("ll", null);

            new2.put("ur", null);
            new2.put("ul", temp.getOrDefault("ul", null));
            new2.put("lr", null);
            new2.put("ll", temp.getOrDefault("ll", null));
        }

        System.out.println("Cut shape one with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + new1.get(key)); 
        }

        System.out.println("Cut shape two with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + new2.get(key)); 
        }

        return new CutResultShape(new1, new2);
    }

    public static class CutResultShape extends Shape {
        private final Shape part1;
        private final Shape part2;

        public CutResultShape(Shape part1, Shape part2) {
            this.part1 = part1;
            this.part2 = part2;
        }

        public Shape getPart1() {
            return part1;
        }

        public Shape getPart2() {
            return part2;
        }
    }
}
