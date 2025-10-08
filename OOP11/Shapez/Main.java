package Shapez;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args){

        List<String> keys = Arrays.asList("ur", "ul", "lr", "ll");
        boolean[] parts = {true, true, true, false};
        Shape shapeExample = new Mine(parts, "red").pull();

        Rotate rotateMachine = new Rotate();
        rotateMachine.push(shapeExample, true);
        Shape rotatedShape = rotateMachine.pull();

        Cut cutMachine = new Cut();
        cutMachine.push(shapeExample, true);
        cutMachine.pull();

        Stick stickMachine = new Stick();
        stickMachine.push(shapeExample, rotatedShape);
        stickMachine.pull();

        // vtora demonstraciq

        boolean[] parts1 = {true, true, false, true};
        Shape shape1 = new Mine(parts1, "green").pull();

        boolean[] parts2 = {true, false, false, true};
        Shape shape2 = new Mine(parts2, "red").pull();

        boolean[] parts3 = {true, false, true, false};
        Shape shape3 = new Mine(parts3, "blue").pull();

        boolean[] parts4 = {false, false, true, false};
        Shape shape4 = new Mine(parts4, "yellow").pull();
        
        //purva figura
        Stick stick1a = new Stick();
        stick1a.push(shape4, shape3);
        Shape combinedShape1a = stick1a.pull();
        Stick stick1b = new Stick();
        stick1b.push(combinedShape1a, shape1);
        Shape newShape1 = stick1b.pull();
        System.out.println("newShape1 created with parts:");
        for (String key : keys) {
            System.out.println(key + ": " + newShape1.get(key));
        }

        //vtora figura
        Rotate rotateShape4 = new Rotate();
        rotateShape4.push(shape4, true);
        Shape rotatedShape4 = rotateShape4.pull();
        Stick stick2 = new Stick();
        stick2.push(shape2, rotatedShape4);
        Shape newShape2 = stick2.pull();
        System.out.println("newShape2 created with parts:");
        for (String key : keys) {
            System.out.println(key + ": " + newShape2.get(key));
        }

        
        // treta output figura
        Stick stick3 = new Stick();
        stick3.push(shape1, shape2);
        Shape result = stick3.pull();
        Stick stick3Dve = new Stick();
        Rotate rotate3 = new Rotate();
        rotate3.push(shape3, true);
        Shape rotated3 = rotate3.pull();
        stick3Dve.push(result, rotated3);
        result = stick3Dve.pull();
        Stick stick3Tri = new Stick();
        stick3Tri.push(result, shape4);
        stick3Tri.pull();

        //chetvurta output figura
        Rotate rotate4 = new Rotate();
        rotate4.push(shape2, true);
        result = rotate4.pull();
        Stick stick4 = new Stick();
        stick4.push(result, shape1);
        result = stick4.pull();
        Rotate rotate4Dve = new Rotate();
        rotate4Dve.push(shape4, true);
        Shape result2 = rotate4Dve.pull();
        Rotate rotate4Tri = new Rotate();
        rotate4Tri.push(result2, true);
        result2 = rotate4Tri.pull();
        Stick stick4Dve = new Stick();
        stick4Dve.push(result, result2);
        stick4Dve.pull(); 

        

    }
}
