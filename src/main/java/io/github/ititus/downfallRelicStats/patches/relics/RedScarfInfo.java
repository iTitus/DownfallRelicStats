package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import hermit.relics.RedScarf;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class RedScarfInfo extends BaseCombatRelicStats {

    private static final RedScarfInfo INSTANCE = new RedScarfInfo();
    private static final int BLOCK_AMOUNT = 2;

    private RedScarfInfo() {
        super(RedScarf.ID);
    }

    public static RedScarfInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RedScarf.class,
            method = "onApplyPower"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, RedScarf.class, "addToBot", Patch.class, false, true);
        }

        public static void after(RedScarf __instance) {
            getInstance().increaseAmount(BLOCK_AMOUNT);
        }
    }
}
