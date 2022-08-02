package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.UnbrokenSoul;

public final class UnbrokenSoulInfo extends BaseCombatRelicStats {

    private static final UnbrokenSoulInfo INSTANCE = new UnbrokenSoulInfo();

    private UnbrokenSoulInfo() {
        super(UnbrokenSoul.ID);
    }

    public static UnbrokenSoulInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = UnbrokenSoul.class,
            method = "onCharge"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainEnergyAction.class, Patch.class, 1);
        }

        public static void hook(int energyAmount) {
            getInstance().increaseAmount(energyAmount);
        }
    }
}
