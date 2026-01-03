package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.AwakenedUrn;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAdjustmentAction;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class AwakenedUrnInfo extends BaseCombatRelicStats {

    private static final AwakenedUrnInfo INSTANCE = new AwakenedUrnInfo();

    private AwakenedUrnInfo() {
        super(AwakenedUrn.ID);
    }

    public static AwakenedUrnInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = AwakenedUrn.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAdjustmentAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AwakenedUrn.class, "addToTop", Patch.class);
        }

        public static void before() {
            preAction = PreAdjustmentAction.fromAdjustment(getInstance(), () -> AbstractDungeon.player.currentHealth);
            AbstractDungeon.actionManager.addToTop(new PostAdjustmentAction(preAction));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToTop(preAction);
        }
    }
}
