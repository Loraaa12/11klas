public class MagicCard extends Card{
    private final IMagicEffect effect;

    public MagicCard(String name, String desc, Player owner, String uniqueId, IMagicEffect effect) {
        super(name, desc, owner, uniqueId);
        if (effect == null) {
            throw new IllegalArgumentException("SpellCard must have an effect.");
        }
        this.effect = effect;
    }

    public void apply(Player owner, MonsterCard target){
        effect.apply(owner, target);
    }
}
