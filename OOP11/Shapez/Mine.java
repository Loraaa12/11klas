package Shapez;

import java.util.List;
import java.util.Arrays;

public class Mine extends Machine{
    private Shape shape;
    private List<String> keys = Arrays.asList("ur", "ul", "lr", "ll");

    public Mine() {
        shape = new Shape();
        
        boolean[] defaultParts = {false, false, false, false};  
        String defaultColor = "empty";
        
        for (int i = 0; i < 4; i++) {
            shape.put(keys.get(i), defaultParts[i] ? defaultColor : null);
        }
    }

    public Mine(boolean[] parts, String color){

        shape = new Shape();

        for(int i = 0; i < 4; i++){
            if(parts[i]){
                shape.put(keys.get(i), color);
            }
            else{
                shape.put(keys.get(i), null);
            }
        }
    }

    public Shape pull(){
        System.out.println("Shape mined with the following parts:");
        for (String key : keys) {
            System.out.println(key + ": " + shape.get(key)); 
        }
        return shape;
    }
}
