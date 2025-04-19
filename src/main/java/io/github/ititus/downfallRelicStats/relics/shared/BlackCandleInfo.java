package io.github.ititus.downfallRelicStats.relics.shared;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import downfall.relics.BlackCandle;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class BlackCandleInfo extends BaseCombatRelicStats {

    private static final BlackCandleInfo INSTANCE = new BlackCandleInfo();

    private BlackCandleInfo() {
        super(BlackCandle.ID);
    }

    public static BlackCandleInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BlackCandle.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(BlackCandle.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
