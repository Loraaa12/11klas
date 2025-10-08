import java.util.*;

public class DialogueGame {

    public static void playGame(Player player, DialogueTree tree) {
        DialogueStep currentStep = tree.getRoot();

        while (currentStep != null) {
            System.out.println("Step ID: " + currentStep.getId());
            System.out.println(currentStep.getNpcReplica());

            if (currentStep.getPlayerOptions() == null || currentStep.getPlayerOptions().isEmpty()) {
                currentStep = currentStep.getNextStep();
                continue;
            }

            List<DialogueOption> availableOptions = new ArrayList<>();
            for (DialogueOption option : currentStep.getPlayerOptions()) {
                if (option.isAvailable(player)) {
                    availableOptions.add(option);
                }
            }

            for (int i = 0; i < availableOptions.size(); i++) {
                System.out.println((i + 1) + ". " + availableOptions.get(i).getPlayerReplica());
            }

            Scanner scanner = new Scanner(System.in);
            int choice;

            while (true) {
                try {
                    System.out.print("Choose an option: ");
                    choice = Integer.parseInt(scanner.nextLine()) - 1;
                    if (choice < 0 || choice >= availableOptions.size()) {
                        throw new IndexOutOfBoundsException();
                    }

                    DialogueOption selectedOption = availableOptions.get(choice);
                    selectedOption.applyRequirements(player);
                    selectedOption.applyRewards(player);

                    currentStep = selectedOption.getNextStep();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid choice or requirement not met. Try again.");
                }
            }
        }

        System.out.println("Dialogue complete.");
    }

    public static void main(String[] args) {
        Player player = new Player(100, 10, 5, 50);
    

        DialogueStep endStep = new DialogueStep("10", "You have reached the end of your journey. Farewell!", null, null);
    

        DialogueOption option9 = new DialogueOption("I will accept the sword.", endStep);
        option9.addRewardModifier(p -> p.addItem("Legendary Sword"));
    
        DialogueOption option8 = new DialogueOption("I will continue without it.", endStep);
    
        DialogueStep step9 = new DialogueStep("9", "Here lies the Sword of Kings. Do you take it?", Arrays.asList(option8, option9), null);
    

        DialogueOption option7 = new DialogueOption("I will take the potion.", step9);
        option7.addRewardModifier(p -> p.addItem("Mysterious Potion"));
    
        DialogueOption option6 = new DialogueOption("I will decline the potion.", step9);
    
        DialogueStep step8 = new DialogueStep("8", "A mysterious figure offers you a potion. Do you accept?", Arrays.asList(option6, option7), null);
    

        DialogueOption option5 = new DialogueOption("Take the left path.", step8);
        DialogueOption option4 = new DialogueOption("Take the right path.", step8);
    
        DialogueStep step7 = new DialogueStep("7", "You come to a fork in the road. Which path do you take?", Arrays.asList(option4, option5), null);
    

        DialogueOption option3 = new DialogueOption("Answer the riddle.", step7);
        option3.addRewardModifier(p -> p.addItem("Golden Key"));
    
        DialogueOption option2 = new DialogueOption("Ignore the riddle.", step7);
    
        DialogueStep step6 = new DialogueStep("6", "A sphinx blocks your path and asks a riddle. Do you answer?", Arrays.asList(option2, option3), null);
    

        DialogueOption option1 = new DialogueOption("Open the chest.", step6);
        option1.addRewardModifier(p -> p.addItem("Silver Coin"));
    
        DialogueOption option0 = new DialogueOption("Leave the chest.", step6);
    
        DialogueStep step5 = new DialogueStep("5", "You find a treasure chest. Do you open it?", Arrays.asList(option0, option1), null);
    

        DialogueOption optionA = new DialogueOption("Buy a health potion.", step5);
        optionA.addRewardModifier(p -> p.addItem("Health Potion"));
        optionA.addRequirementModifier(new IRequirement() {
            @Override
            public boolean test(Player player) {
                return player.getGold() >= 10;
            }
    
            @Override
            public void take(Player player) {
                player.setGold(player.getGold() - 10);
            }
        });
    
        DialogueOption optionB = new DialogueOption("Continue on your journey.", step5);
    
        DialogueStep step4 = new DialogueStep("4", "A friendly merchant offers you a health potion for 10 gold. Do you buy it?", Arrays.asList(optionA, optionB), null);
    

        DialogueOption optionC = new DialogueOption("Cross the bridge.", step4);
        optionC.addRequirementModifier(new IRequirement() {
            @Override
            public boolean test(Player player) {
                return player.getStr() >= 10;
            }
    
            @Override
            public void take(Player player) {
            }
        });
    
        DialogueOption optionD = new DialogueOption("Find another way.", step4);
    
        DialogueStep step3 = new DialogueStep("3", "You encounter a rickety bridge. Do you attempt to cross it?", Arrays.asList(optionC, optionD), null);
    

        DialogueOption optionE = new DialogueOption("Enter the cave.", step3);
        DialogueOption optionF = new DialogueOption("Continue on the path.", step3);
    
        DialogueStep step2 = new DialogueStep("2", "You see a mysterious cave. Do you enter?", Arrays.asList(optionE, optionF), null);
    

        DialogueStep root = new DialogueStep("1", "Welcome, brave adventurer. The path ahead is perilous.", null, step2);
    
        DialogueTree tree = new DialogueTree(root);
    
        playGame(player, tree);
    }
}