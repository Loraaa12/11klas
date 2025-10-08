public class Road{
    Node node1, node2;
    String roadType;

    public Road(Node node1, Node node2, String roadType) {
        this.node1 = node1;
        this.node2 = node2;
        this.roadType = roadType; // "asphalt" or "rail"
    }

    public boolean connects(Node node) {
        return node == node1 || node == node2;
    }

    public Node getOtherNode(Node node) {
        if (node == node1) return node2;
        if (node == node2) return node1;
        throw new IllegalArgumentException("Node not connected to this road");
    }
}