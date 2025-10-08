public class Crossroad extends Node{
    int currentRoadIndex = 0;

    public Crossroad(String name) {
        super(name);
    }

    public Road getOpenRoad() {
        if (roads.isEmpty()) return null;
        Road road = roads.get(currentRoadIndex);
        currentRoadIndex = (currentRoadIndex + 1) % roads.size();
        return road;
    }
}