import java.util.*;

public class Node{
    String name;
    List<Road> roads = new ArrayList<>();
    Queue<Vehicle> vehicles = new LinkedList<>();

    public Node(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
            return name;
        }
}