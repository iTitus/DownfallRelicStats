package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import guardian.actions.BraceAction;
import guardian.relics.DefensiveModeMoreBlock;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.FieldAccessHookEditor;
import javassist.expr.ExprEditor;

public final class DefensiveModeMoreBlockInfo extends BaseCombatRelicStats {

    private static final DefensiveModeMoreBlockInfo INSTANCE = new DefensiveModeMoreBlockInfo();

    private DefensiveModeMoreBlockInfo() {
        super(DefensiveModeMoreBlock.ID);
    }

    public static DefensiveModeMoreBlockInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BraceAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new FieldAccessHookEditor(2, BraceAction.class, "braceValue", Patch.class);
        }

        public static int hook(BraceAction __instance, int braceValue) {
            getInstance().registerStartingAmount(__instance.braceValue);
            getInstance().registerEndingAmount(braceValue);
            return braceValue;
        }
    }
}
