package io.github.ititus.downfallRelicStats.relics;

import automaton.relics.PlatinumCore;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class PlatinumCoreInfo extends BaseCombatRelicStats {

    private static final PlatinumCoreInfo INSTANCE = new PlatinumCoreInfo();

    private PlatinumCoreInfo() {
        super(PlatinumCore.ID);
    }

    public static PlatinumCoreInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = PlatinumCore.class,
            method = "receiveCompile"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(PlatinumCore.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
