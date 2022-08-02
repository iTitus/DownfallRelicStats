package io.github.ititus.downfallRelicStats.patches.relics;

import automaton.relics.BronzeCore;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class BronzeCoreInfo extends BaseCombatRelicStats {

    private static final BronzeCoreInfo INSTANCE = new BronzeCoreInfo();

    private BronzeCoreInfo() {
        super(BronzeCore.ID);
    }

    public static BronzeCoreInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BronzeCore.class,
            method = "receiveCompile"
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
