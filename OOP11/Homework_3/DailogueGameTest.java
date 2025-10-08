import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

//samo che ne bachkat??

class DialogueGameTest {

    @Test
    void testDialogueStepValidation() {
        DialogueStep endStep = new DialogueStep("10", "End", null, null);
        DialogueOption option = new DialogueOption("Continue", endStep);

        assertThrows(IllegalArgumentException.class, () -> {
            new DialogueStep("1", "Invalid step", Arrays.asList(option), endStep);
        });
    }

    @Test
    void testDialogueOptionRequirements() {
        Player player = new Player(100, 10, 5, 50);
        DialogueStep nextStep = new DialogueStep("2", "Next step", null, null);

        DialogueOption option = new DialogueOption("Continue", nextStep);
        option.addRequirementModifier(new IRequirement() {
            @Override
            public boolean test(Player player) {
                return player.getGold() >= 50;
            }

            @Override
            public void take(Player player) {
                player.setGold(player.getGold() - 50);
            }
        });

        assertTrue(option.isAvailable(player));

        option.applyRequirements(player);
        assertEquals(0, player.getGold());
    }

    @Test
    void testDialogueOptionRewards() {
        Player player = new Player(100, 10, 5, 50);
        DialogueStep nextStep = new DialogueStep("2", "Next step", null, null);

        DialogueOption option = new DialogueOption("Continue", nextStep);
        option.addRewardModifier(p -> p.addItem("Health Potion"));

        option.applyRewards(player);
        assertTrue(player.getInventory().contains("Health Potion"));
    }

    @Test
    void testDialogueTreeNavigation() {
        Player player = new Player(100, 10, 5, 50);

        DialogueStep endStep = new DialogueStep("10", "End", null, null);
        DialogueOption option1 = new DialogueOption("Option 1", endStep);
        DialogueOption option2 = new DialogueOption("Option 2", endStep);

        DialogueStep root = new DialogueStep("1", "Start", Arrays.asList(option1, option2), null);
        DialogueTree tree = new DialogueTree(root);

        assertEquals(root, tree.getRoot());
        assertEquals(endStep, tree.findById("10"));
    }

    @Test
    void testPlayerInventory() {
        Player player = new Player(100, 10, 5, 50);
        player.addItem("Sword");
        assertTrue(player.getInventory().contains("Sword"));

        player.removeItem("Sword");
        assertFalse(player.getInventory().contains("Sword"));
    }
}