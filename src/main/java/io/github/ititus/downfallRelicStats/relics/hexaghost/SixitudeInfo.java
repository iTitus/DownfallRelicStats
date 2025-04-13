package io.github.ititus.downfallRelicStats.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.Sixitude;

public final class SixitudeInfo extends BaseCombatRelicStats {

    private static final SixitudeInfo INSTANCE = new SixitudeInfo();

    private SixitudeInfo() {
        super(Sixitude.ID);
    }

    public static SixitudeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Sixitude.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(Sixitude.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
