package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.SpiritBrand;

public final class SpiritBrandInfo extends BaseCombatRelicStats {

    private static final SpiritBrandInfo INSTANCE = new SpiritBrandInfo();

    private SpiritBrandInfo() {
        super(SpiritBrand.ID);
    }

    public static SpiritBrandInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SpiritBrand.class,
            method = "onCharge"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(blockAmount);
        }
    }
}
