package io.github.ititus.downfallRelicStats.patches.relics;

import champ.relics.FightingForDummies;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class FightingForDummiesInfo extends BaseCombatRelicStats {

    private static final FightingForDummiesInfo INSTANCE = new FightingForDummiesInfo();

    private FightingForDummiesInfo() {
        super(FightingForDummies.ID);
    }

    public static FightingForDummiesInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = FightingForDummies.class,
            method = "onPlayerEndTurn"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(FightingForDummies.class, "addToBot", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
