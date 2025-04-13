package io.github.ititus.downfallRelicStats.relics.automaton;

import automaton.relics.ProtectiveGoggles;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class ProtectiveGogglesInfo extends BaseCombatRelicStats {

    private static final ProtectiveGogglesInfo INSTANCE = new ProtectiveGogglesInfo();

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
            return new ConstructorHookEditor(GainBlockAction.class, Patch.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(blockAmount);
        }
    }
}
