import java.util.*;

@FunctionalInterface
interface IMagicEffect {
    void apply(Player owner, MonsterCard target);
}

