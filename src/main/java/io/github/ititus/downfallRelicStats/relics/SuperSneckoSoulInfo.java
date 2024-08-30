package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import sneckomod.relics.SuperSneckoSoul;

public final class SuperSneckoSoulInfo extends BaseCombatRelicStats {

    private static final SuperSneckoSoulInfo INSTANCE = new SuperSneckoSoulInfo();

    private SuperSneckoSoulInfo() {
        super(SuperSneckoSoul.ID);
    }

    public static SuperSneckoSoulInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SuperSneckoSoul.class,
            method = "onUseCard"
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
