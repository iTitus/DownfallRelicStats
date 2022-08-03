package io.github.ititus.downfallRelicStats.patches.relics;

import champ.relics.DuelingGlove;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class DuelingGloveInfo extends BaseCombatRelicStats {

    private static final DuelingGloveInfo INSTANCE = new DuelingGloveInfo();

    private DuelingGloveInfo() {
        super(DuelingGlove.ID);
    }

    public static DuelingGloveInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DuelingGlove.class,
            method = "onPlayCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(VulnerablePower.class, Patch.class, 2);
        }

        public static void hook(int amount) {
            getInstance().increaseAmount(amount);
        }
    }
}
