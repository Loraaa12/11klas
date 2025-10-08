public abstract class Card {
    private final String name;
    private final String desc;
    private final String uniqueId;
    private Player owner;

    public Card(String name, String desc, Player owner, String uniqueId){
        this.name = name;
        this.desc = desc;
        this.owner = owner;
        this.uniqueId = uniqueId;
    };

    public Player getOwner() {
        return owner;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
