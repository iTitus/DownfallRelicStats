package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.WhiteRibbon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class WhiteRibbonInfo extends BaseCombatRelicStats {

    private static final WhiteRibbonInfo INSTANCE = new WhiteRibbonInfo();

    private WhiteRibbonInfo() {
        super(WhiteRibbon.ID);
    }

    public static WhiteRibbonInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = WhiteRibbon.class,
            method = "onPlayCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAdjustmentAction preAction;

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(blockAmount);
        }
    }
}
