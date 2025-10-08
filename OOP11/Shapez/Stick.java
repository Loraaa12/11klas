package Shapez;

import java.util.Arrays;
import java.util.List;

public class Stick {
    private Shape temp1;
    private Shape temp2;
    private Shape newShape;
    private List<String> keys = Arrays.asList("ur", "ul", "lr", "ll");

    public void push(Shape temp1, Shape temp2){
        this.temp1 = temp1;
        this.temp2 = temp2;

        System.out.println("Stick push shape one with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + temp1.get(key)); 
        }

        System.out.println("Stick push shape two with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + temp2.get(key)); 
        }
        
        newShape = new Shape();
    }

    public Shape pull(){
        System.out.println("Shape stuck with the following parts:");

        for (String key : keys) {
            String value = temp2.get(key) != null ? temp2.get(key) : temp1.get(key);
            newShape.put(key, value);
            System.out.println(key + ": " + newShape.get(key)); 
        }

        return newShape;
    }
}
