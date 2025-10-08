import java.util.*;

class DialogueStep {
    private String id;
    private String npcReplica;
    private List<DialogueOption> playerOptions;
    private DialogueStep nextStep;

    public DialogueStep(String id, String npcReplica, List<DialogueOption> playerOptions, DialogueStep nextStep) {
        //if ((playerOptions != null && nextStep != null) || (playerOptions == null && nextStep == null && !"5".equals(id))) {
            //throw new IllegalArgumentException("A step must have either player options or a next step, but not both.");
        //}
        if (npcReplica == null || npcReplica.isEmpty()) {
            throw new IllegalArgumentException("NPC replica cannot be null or empty.");
        }
        this.id = id;
        this.npcReplica = npcReplica;
        this.playerOptions = playerOptions;
        this.nextStep = nextStep;
    }

    public String getId() {
        return id;
    }

    public String getNpcReplica() {
        return npcReplica;
    }

    public List<DialogueOption> getPlayerOptions() {
        return playerOptions;
    }

    public DialogueStep getNextStep() {
        return nextStep;
    }
}