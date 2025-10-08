import java.util.*;

class Player {
    private int hp;
    private int str;
    private int cha;
    private int gold;
    private List<String> inventory = new ArrayList<>();

    public Player(int hp, int str, int cha, int gold) {
        this.hp = hp;
        this.str = str;
        this.cha = cha;
        this.gold = gold;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getCha() {
        return cha;
    }

    public void setCha(int cha) {
        this.cha = cha;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public void removeItem(String item) {
        if (!inventory.remove(item)) {
            throw new IllegalArgumentException("Required item not found in inventory: " + item);
        }
    }
}