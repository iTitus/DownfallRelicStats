package io.github.ititus.downfallRelicStats.patches.relics;

import collector.relics.RoughDiamond;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class RoughDiamondInfo extends BaseCombatRelicStats {

    private static final RoughDiamondInfo INSTANCE = new RoughDiamondInfo();

    private RoughDiamondInfo() {
        super(RoughDiamond.ID);
    }

    public static RoughDiamondInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RoughDiamond.class,
            method = "onPlayCard"
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
