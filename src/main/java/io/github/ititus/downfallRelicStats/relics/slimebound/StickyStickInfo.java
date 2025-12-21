package io.github.ititus.downfallRelicStats.relics.slimebound;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import slimebound.relics.StickyStick;

public final class StickyStickInfo extends BaseCombatRelicStats {

    private static final StickyStickInfo INSTANCE = new StickyStickInfo();

    private StickyStickInfo() {
        super(StickyStick.ID);
    }

    public static StickyStickInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = StickyStick.class,
            method = "onCardDraw"
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
