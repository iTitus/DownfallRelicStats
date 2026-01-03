package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.NerfedMummifiedHand;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class NerfedMummifiedHandInfo extends BaseCombatRelicStats {

    private static final NerfedMummifiedHandInfo INSTANCE = new NerfedMummifiedHandInfo();

    private NerfedMummifiedHandInfo() {
        super(NerfedMummifiedHand.ID);
    }

    public static NerfedMummifiedHandInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = NerfedMummifiedHand.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractCard.class, "setCostForTurn", Patch.class, true, false).addReceiver();
        }

        public static void before(AbstractCard c) {
            if (c.costForTurn > 0 && !c.freeToPlay()) {
                getInstance().increaseAmount(c.costForTurn);
            }
        }
    }
}
