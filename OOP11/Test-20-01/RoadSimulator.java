import java.util.*

public class RoadSimulator{
    Map<String, Depot> depots = new HashMap<>();
    Map<String, Crossroad> crossroads = new HashMap<>();
    List<Road> roads = new ArrayList<>();
    List<Vehicle> vehicles = new ArrayList<>();


    public void readFile(Scanner scanner) {
        String mode = "depots";

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                mode = switch (mode) {
                    case "depots" -> "crossroads";
                    case "crossroads" -> "connections";
                    default -> mode;
                };
                continue;
            }


            switch (mode) {
                case "depots" -> depots.put(line, new Depot(line));
                case "crossroads" -> crossroads.put(line, new Crossroad(line));
                case "connections" -> {
                    String[] parts = line.split(" ");
                    if (parts.length != 2) throw new IllegalArgumentException("Invalid connection format");
                    Node node1 = depots.getOrDefault(parts[0], crossroads.get(parts[0]));
                    Node node2 = depots.getOrDefault(parts[1], crossroads.get(parts[1]));
                    if (node1 == null || node2 == null) throw new IllegalArgumentException("Invalid connection between " + parts[0] + " and " + parts[1]);

                    Road road = new Road(node1, node2, "asphalt");
                    roads.add(road);
                    node1.roads.add(road);
                    node2.roads.add(road);
                }
            }
        }

        validateNetwork();
    }

    private void validateNetwork(){
        Set<Node> visited = new HashMap<>();
        Node startNode = depots.values().stream().findFirst().orElseThrow(() -> new IllegalStateException("No depots found"));

        dfs(startNode, visited);

        Set<Node> allNodes = new HashSet<>(depots.values());
        allNodes.addAll(crossroads.values());

        if (!visited.equals(allNodes)) {
            throw new IllegalStateException("Network is not fully connected");
        }
    }

    private void dfs(Node node, Set<Node> visited) {
        if (visited.contains(node)) return;
        visited.add(node);
        for (Road road : node.roads) {
            dfs(road.getOtherNode(node), visited);
        }
    }

    void simulate(int cycleCount, int vehicleCount){
        Random random = New Random();

        for (int cycle = 1; cycle <= cycleCount; cycle++) {
            int carsCreated = 0;
            int emergencyCarsCreated = 0;
            int trainsCreated = 0;


            for (int i = 0; i < vehicleCount; i++) {
                Depot start = getRandomDepot(random);
                Depot destination;
                do {
                    destination = getRandomDepot(random);
                } while (destination == start);

                Vehicle vehicle = random.nextBoolean() ? new Car(start, destination) : new EmergencyCar(start, destination): new Train(start, destination);
                vehicles.add(vehicle);

                if (vehicle instanceof Car) carsCreated++;
                else if(vehicle instanceof Train) trainsCreated++;
                else emergencyCarsCreated++;
            }

            int arrived = 0;
            Iterator<Vehicle> iterator = vehicles.iterator();
            while (iterator.hasNext()) {
                Vehicle vehicle = iterator.next();
                if (vehicle.currentLocation == vehicle.destination) {
                    iterator.remove();
                    arrived++;
                }
            }

            System.out.printf("%d cars created %n", carsCreated);
            System.out.printf("%d emergency cars created%n", emergencyCarsCreated);
            System.out.printf("%d trains created%n", trainsCreated);
            System.out.printf("%d vehicles arrived at their destination%n", arrived);
            System.out.printf("%d vehicles exist%n",vehicles.size());
            System.out.printf("The most congested crossroad is: %s", getMostCongestedCrossroad())
        }
}

    private Depot getRandomDepot(Random random) {
        List<Depot> depotList = new ArrayList<>(depots.values());
        return depotList.get(random.nextInt(depotList.size()));
    }

    private String getMostCongestedCrossroad(){
        return crossroads.values().stream().max(Comparator.comparingInt(c -> c.vehicles.size())).map(Node::toString).orElse("none of them");
    }

    public static void main(String args[]){
        RoadSimulator simulator = new RoadSimulator();
        Scanner scanner = new Scanner(System.in);

        try{
            simulator.readFile(scanner);
            simulator.simulate(6,12);
        }
        catch(Exception e){
            System.err.println("Error: "+e.getMessage());
        }
    }
}


