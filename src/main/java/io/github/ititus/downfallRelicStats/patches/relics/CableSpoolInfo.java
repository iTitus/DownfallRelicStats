package io.github.ititus.downfallRelicStats.patches.relics;

import automaton.relics.CableSpool;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class CableSpoolInfo extends BaseCombatRelicStats {

    private static final CableSpoolInfo INSTANCE = new CableSpoolInfo();

    private CableSpoolInfo() {
        super(CableSpool.ID);
        this.showPerTurn = false;
    }

    public static CableSpoolInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CableSpool.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, CableSpool.class, "addToBot", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
