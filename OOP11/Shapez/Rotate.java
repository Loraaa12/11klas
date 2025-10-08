package Shapez;

import java.util.Arrays;
import java.util.List;

public class Rotate extends Machine {
    private Shape newShape;
    private Shape temp;
    private boolean isClockwise;
    private List<String> keys = Arrays.asList("ur", "ul", "lr", "ll");

    public void push(Shape shape, boolean direction) {
        this.isClockwise = direction;
        temp = shape;
        newShape = new Shape();
    
        if (temp == null) {
            System.out.println("Error: Shape is null.");
            return; 
        }
    
        System.out.println("Rotate push shape with the following parts:");
        for (String key : keys) {
            String value = temp.get(key);
            if (value != null) {
                System.out.println(key + ": " + value);
            } else {
                System.out.println(key + ": null");
            }
        }
    }
    
    public Shape pull(){
        if (newShape == null) {
            newShape = new Shape();
        }
        if(isClockwise == true){
            newShape.put("ur", temp.get("ul"));
            newShape.put("ul", temp.get("ll"));
            newShape.put("lr", temp.get("ur"));
            newShape.put("ll", temp.get("lr"));
        }
        else{
            newShape.put("ur", temp.get("lr"));
            newShape.put("ul", temp.get("ur"));
            newShape.put("ll", temp.get("ul"));
            newShape.put("lr", temp.get("ll"));
        }

        System.out.println("Rotated shape with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + newShape.get(key)); 
        }

        return newShape;
    }
}
