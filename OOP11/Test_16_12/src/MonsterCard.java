public class MonsterCard extends Card{
    private int atk;
    private int def;
    private int current_hp;
    private final int max_hp;

    public MonsterCard(String name, String desc, Player owner, String uniqueId, int atk, int def, int max_hp){
        super(name, desc, owner, uniqueId);
        this.atk = atk;
        this.def = def;
        this.current_hp = current_hp;
        this.max_hp = max_hp;
    }

    public void attack(Player opp){
            MonsterCard defender = opp.getFirstMonsterOnField();
            if (defender != null) {
                int damageToDefender = Math.max(1, this.atk - defender.getDef());
                defender.setCurrent_hp(defender.getCurrent_hp() - damageToDefender);

                if (defender.getCurrent_hp() <= 0) {
                    opp.removeMonsterFromField(defender);
                } else {
                    int damageToAttacker = Math.max(1, defender.getAtk() - this.def);
                    this.setCurrent_hp(this.getCurrent_hp() - damageToAttacker);

                    if (this.getCurrent_hp() <= 0) {
                        this.getOwner().removeMonsterFromField(this);
                    }
                }
            } else {
                opp.decreaseHealth(this.atk);
            }
    }

    public void setAtk(int new_atk){
        atk = Math.min(20, Math.max(0, new_atk));
    }

    public void setDef(int new_def){
        def = Math.min(20, Math.max(0, new_def));
    }

    public void setCurrent_hp(int new_hp){
        current_hp = Math.min(max_hp, new_hp);
    }

    public int getAtk(){
        return atk;
    }

    public int getDef(){
        return def;
    }

    public int getCurrent_hp() {
        return current_hp;
    }

    public int getMax_hp() {
        return max_hp;
    }
}
