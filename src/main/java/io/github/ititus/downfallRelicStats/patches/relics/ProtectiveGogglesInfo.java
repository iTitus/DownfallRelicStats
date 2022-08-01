package io.github.ititus.downfallRelicStats.patches.relics;

import automaton.relics.ProtectiveGoggles;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class ProtectiveGogglesInfo extends BaseCombatRelicStats {

    private static final ProtectiveGogglesInfo INSTANCE = new ProtectiveGogglesInfo();
    private static final int BLOCK_AMOUNT = 4;

    private ProtectiveGogglesInfo() {
        super(ProtectiveGoggles.ID);
    }

    public static ProtectiveGogglesInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ProtectiveGoggles.class,
            method = "onPlayerEndTurn"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, ProtectiveGoggles.class, "addToTop", Patch.class, false, true);
        }

        public static void after(ProtectiveGoggles __instance) {
            getInstance().increaseAmount(BLOCK_AMOUNT);
        }
    }
}
