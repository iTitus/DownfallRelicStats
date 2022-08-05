package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import guardian.actions.StasisCodexAction;
import guardian.relics.StasisCodex;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class StasisCodexInfo extends BaseCombatRelicStats {

    private static final StasisCodexInfo INSTANCE = new StasisCodexInfo();

    private StasisCodexInfo() {
        super(StasisCodex.ID);
    }

    public static StasisCodexInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = StasisCodexAction.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToTop", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
