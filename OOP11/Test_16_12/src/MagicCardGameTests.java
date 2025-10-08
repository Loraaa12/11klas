import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MagicCardGameTests {

    private Player player1;
    private Player player2;
    private MonsterCard monster1;
    private MonsterCard monster2;
    private MagicCard spell1;
    private MagicCard spell2;

    @BeforeEach
    void setUp() {
        player1 = new Player(50);
        player2 = new Player(50);

        monster1 = new MonsterCard("Dragon", "A fierce dragon",  player1, "M001",15, 10, 30);
        monster2 = new MonsterCard("Goblin", "A sneaky goblin",  player2, "M002",10, 5, 20);

        spell1 = new MagicCard("Fireball", "Burns the enemy",  player1, "S001",ExampleEffects.decreaseDefense(5));
        spell2 = new MagicCard("Healing", "Restores health",  player2,"S002", ExampleEffects.destroyIfLowHealth());

        player1.addMonsterToDeck(monster1);
        player2.addMonsterToDeck(monster2);
        player1.addSpellToDeck(spell1);
        player2.addSpellToDeck(spell2);
    }

    @Test
    void testDrawMonster() {
        player1.drawMonster();
        assertEquals(1, player1.monsterHand.size());
        assertEquals(monster1, player1.monsterHand.get(0));
    }


    @Test
    void testDrawSpell() {
        player1.drawSpell();
        assertEquals(1, player1.spellHand.size());
        assertEquals(spell1, player1.spellHand.get(0));
    }

    @Test
    void testPlayMonster() {
        player1.drawMonster();
        player1.playMonster(monster1);

        assertTrue(player1.monsterHand.isEmpty());
        assertEquals(1, player1.field.size());
        assertEquals(monster1, player1.field.get(0));
    }

    @Test
    void testPlaySpell() {
        player1.drawMonster();
        player2.drawMonster();
        player1.drawSpell();
        player1.playMonster(monster1);
        player2.playMonster(monster2);

        player1.playSpell(spell1, player2, monster2);

        assertEquals(0, player1.spellHand.size());
        assertEquals(0, player2.field.get(0).getDef()); // Defense reduced by 5
    }

    @Test
    void testAttackReducesDefenderHealth() {
        player1.drawMonster();
        player2.drawMonster();

        player1.playMonster(monster1);
        player2.playMonster(monster2);

        monster1.attack(player2);

        assertEquals(13, monster2.getCurrent_hp());
    }

    @Test
    void testAttackRemovesDefeatedMonster() {
        monster1.setAtk(30); // Ensure the attack is lethal
        player1.drawMonster();
        player2.drawMonster();

        player1.playMonster(monster1);
        player2.playMonster(monster2);

        monster1.attack(player2);

        assertTrue(player2.field.isEmpty());
    }

    @Test
    void testAttackDirectlyHitsPlayer() {
        player1.drawMonster();
        player1.playMonster(monster1);

        monster1.attack(player2);

        assertEquals(35, player2.getHealth()); // Player takes damage directly
    }

    @Test
    void testSpellDestroysLowHealthMonster() {
        monster1.setCurrent_hp(3); // Make monster weak
        player2.addMonsterToDeck(monster1);
        player2.drawMonster();
        player2.playMonster(monster1);

        player2.drawSpell();
        player2.playSpell(spell2,player1, monster1);

        assertTrue(player2.field.isEmpty()); // Monster is destroyed
    }

    @Test
    void testSpellDoesNotDestroyHighHealthMonster() {
        monster1.setCurrent_hp(10); // Make monster strong
        player2.addMonsterToDeck(monster1);
        player2.drawMonster();
        player2.playMonster(monster1);

        player2.drawSpell();
        player2.playSpell(spell2,player1, monster1);

        assertFalse(player2.field.isEmpty()); // Monster is not destroyed
    }


    @Test
    void testMonsterSetterBounds() {
        monster1.setAtk(25); // Should cap at 20
        assertEquals(20, monster1.getAtk());

        monster1.setAtk(-5); // Should cap at 0
        assertEquals(0, monster1.getAtk());

        monster1.setDef(25); // Should cap at 20
        assertEquals(20, monster1.getDef());

        monster1.setDef(-5); // Should cap at 0
        assertEquals(0, monster1.getDef());
    }
}
