public class MagicCardGame {
    public static void main(String[] args) {
        Player player1 = new Player(50);
        Player player2 = new Player(50);

        // Add cards to players
        MonsterCard monster1 = new MonsterCard("Dragon", "A fierce dragon",  player1,"M001", 15, 10, 30);
        MonsterCard monster2 = new MonsterCard("Goblin", "A sneaky goblin", player2, "M002",10, 5, 20);

        player1.addMonsterToDeck(monster1);
        player2.addMonsterToDeck(monster2);

        MagicCard spell1 = new MagicCard("Fireball", "Burns the enemy",  player1,"S001", ExampleEffects.decreaseDefense(5));
        MagicCard spell2 = new MagicCard("Healing", "Restores health",  player2,"S002", ExampleEffects.destroyIfLowHealth());

        player1.addSpellToDeck(spell1);
        player2.addSpellToDeck(spell2);

        // Draw cards
        player1.drawMonster();
        player2.drawMonster();
        player1.drawSpell();
        player2.drawSpell();

        // Play a monster
        player1.playMonster(monster1);
        player2.playMonster(monster2);

        // Attack and spell usage
        monster1.attack(player2);
        player1.playSpell(spell1, player2, monster2);
        player2.playSpell(spell2, player1, monster1);

        System.out.println("Player 1 health: " + player1.getHealth());
        System.out.println("Player 2 health: " + player2.getHealth());
    }
}

