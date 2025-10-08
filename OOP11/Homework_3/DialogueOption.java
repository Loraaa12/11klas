import java.util.*;

class DialogueOption {
    private String playerReplica;
    private DialogueStep nextStep;
    private List<IOptional> optionalModifiers = new ArrayList<>();
    private List<IReward> rewardModifiers = new ArrayList<>();
    private List<IRequirement> requirementModifiers = new ArrayList<>();

    public DialogueOption(String playerReplica, DialogueStep nextStep) {
        this.playerReplica = playerReplica;
        this.nextStep = nextStep;
    }

    public String getPlayerReplica() {
        return playerReplica;
    }

    public DialogueStep getNextStep() {
        return nextStep;
    }

    public void addOptionalModifier(IOptional modifier) {
        optionalModifiers.add(modifier);
    }

    public void addRewardModifier(IReward modifier) {
        rewardModifiers.add(modifier);
    }

    public void addRequirementModifier(IRequirement modifier) {
        requirementModifiers.add(modifier);
    }

    public boolean isAvailable(Player player) {
        boolean optionalConditionsMet = optionalModifiers.stream()
                .allMatch(modifier -> modifier.test(player));

        boolean requirementsMet = requirementModifiers.stream()
                .allMatch(modifier -> modifier.test(player));

        return optionalConditionsMet && requirementsMet;
    }

    public void applyRequirements(Player player) {
        for (IRequirement modifier : requirementModifiers) {
            modifier.take(player);
        }
    }

    public void applyRewards(Player player) {
        for (IReward modifier : rewardModifiers) {
            modifier.reward(player);
        }
    }
}