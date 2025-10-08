import java.util.*;
public class Player {
    public final List<MonsterCard> monsterHand = new ArrayList<>();
    public final List<MagicCard> spellHand = new ArrayList<>();
    public final Deque<MonsterCard> monsterDeck = new ArrayDeque<>();
    public final Deque<MagicCard> spellDeck = new ArrayDeque<>();
    public final List<MonsterCard> field = new ArrayList<>();
    private int health;

    public Player(int health){
        this.health = health;
    }

    public void drawMonster() {
        if (monsterHand.size() < 5 && !monsterDeck.isEmpty()) {
            monsterHand.add(monsterDeck.poll());
        }
    }

    public void drawSpell() {
        if (spellHand.size() < 3 && !spellDeck.isEmpty()) {
            spellHand.add(spellDeck.poll());
        }
    }


    public void playMonster(MonsterCard monster) {
        if (monsterHand.remove(monster)) {
            field.add(monster);
            drawMonster();
        }
    }

    public void playSpell(MagicCard spell,Player owner, MonsterCard target) {
        if (spellHand.remove(spell)) {
            spell.apply(owner, target);
            drawSpell();
        }
    }

    public MonsterCard getFirstMonsterOnField() {
        return field.isEmpty() ? null : field.getFirst();
    }

    public void removeMonsterFromField(MonsterCard monster) {
        field.remove(monster);
    }

    public void decreaseHealth(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }

    public void addMonsterToDeck(MonsterCard monster) {
        monsterDeck.add(monster);
    }

    public void addSpellToDeck(MagicCard spell) {
        spellDeck.add(spell);
    }
}
