package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.MiniBlackHole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAdjustmentAction;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class MiniBlackHoleInfo extends BaseCombatRelicStats {

    private static final MiniBlackHoleInfo INSTANCE = new MiniBlackHoleInfo();

    private MiniBlackHoleInfo() {
        super(MiniBlackHole.ID);
    }

    public static MiniBlackHoleInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MiniBlackHole.class,
            method = "atTurnStartPostDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAdjustmentAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, MiniBlackHole.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = PreAdjustmentAction.fromAdjustment(getInstance(), () -> AbstractDungeon.player.hand.group.size()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
