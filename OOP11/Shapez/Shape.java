package Shapez;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shape {
    private Map<String, String> shape;
    private List<String> keys = Arrays.asList("ur", "ul", "lr", "ll");

    public Shape() {
        this.shape = new HashMap<>();
        this.shape.put("ur", null);
        this.shape.put("ul", null);
        this.shape.put("lr", null);
        this.shape.put("ll", null);
    }

    public Shape(Map<String, String> shape) {
        this.shape = new HashMap<>(shape);
    }

    public String get(String part) {
        return shape.get(part);
    }

    public void put(String part, String color) {
        shape.put(part, color);
    }

    public String getOrDefault(String part, String defaultValue) {
        return shape.getOrDefault(part, defaultValue);
    }

    public Map<String, String> getShape() {
        return shape;
    }

    public void printShape(){

        for (String key : keys) {
            System.out.println(key + ": " + shape.get(key)); 
        }
    }
}
