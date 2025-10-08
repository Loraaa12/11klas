class ExampleEffects {
    public static IMagicEffect increaseAttack(int amount) {
        return (owner, target) -> target.setAtk(target.getAtk() + amount);
    }

    public static IMagicEffect decreaseDefense(int amount) {
        return (owner, target) -> target.setDef(target.getDef() - amount);
    }

    public static IMagicEffect destroyIfLowHealth() {
        return (owner, target) -> {
            if (target.getCurrent_hp() < 4) {
                owner.removeMonsterFromField(target);
            }
        };
    }

    public static IMagicEffect printPonichka() {
        return (owner, target) -> System.out.println("ponichka");
    }
}

