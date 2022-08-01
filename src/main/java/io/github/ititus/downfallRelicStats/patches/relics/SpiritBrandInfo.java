package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.SpiritBrand;

public final class SpiritBrandInfo extends BaseCombatRelicStats {

    private static final SpiritBrandInfo INSTANCE = new SpiritBrandInfo();
    private static final int BLOCK_AMOUNT = 4;

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
            return new BeforeAfterMethodCallEditor(0, SpiritBrand.class, "addToBot", Patch.class, false, true);
        }

        public static void after(SpiritBrand __instance) {
            getInstance().increaseAmount(BLOCK_AMOUNT);
        }
    }
}
