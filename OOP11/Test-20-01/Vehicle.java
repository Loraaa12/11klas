abstract clas Vehicle{
    static int idCounter = 0;
    int id;
    Node start, destination;
    Node currentLocation;

    public Vehicle(Node start, Node destination) {
        this.id = ++idCounter;
        this.start = start;
        this.destination = destination;
        this.currentLocation = start;
    }

    public void move(Node nextNode) {
    this.currentLocation = nextNode;
}
}