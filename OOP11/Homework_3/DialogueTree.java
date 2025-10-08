class DialogueTree {
    private DialogueStep root;

    public DialogueTree(DialogueStep root) {
        this.root = root;
    }

    public DialogueStep findById(String id) {
        return findById(root, id);
    }

    private DialogueStep findById(DialogueStep step, String id) {
        if (step == null) return null;
        if (step.getId().equals(id)) return step;
        if (step.getPlayerOptions() != null) {
            for (DialogueOption option : step.getPlayerOptions()) {
                DialogueStep result = findById(option.getNextStep(), id);
                if (result != null) return result;
            }
        } else if (step.getNextStep() != null) {
            return findById(step.getNextStep(), id);
        }
        return null;
    }

    public DialogueStep getRoot() {
        return root;
    }
}