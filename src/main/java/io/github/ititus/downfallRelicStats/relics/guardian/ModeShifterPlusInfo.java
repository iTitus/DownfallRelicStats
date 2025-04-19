package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import guardian.relics.ModeShifterPlus;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class ModeShifterPlusInfo extends BaseCombatRelicStats {

    private static final ModeShifterPlusInfo INSTANCE = new ModeShifterPlusInfo();

    private ModeShifterPlusInfo() {
        super(ModeShifterPlus.ID);
    }

    public static ModeShifterPlusInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ModeShifterPlus.class,
            method = "onChangeStance"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ModeShifterPlus.class, "addToTop", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
