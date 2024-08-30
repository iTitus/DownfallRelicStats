package io.github.ititus.downfallRelicStats.relics.shared;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import downfall.relics.HeartBlessingBlue;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAdjustmentAction;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class HeartBlessingBlueInfo extends BaseCombatRelicStats {

    private static final HeartBlessingBlueInfo INSTANCE = new HeartBlessingBlueInfo();

    private HeartBlessingBlueInfo() {
        super(HeartBlessingBlue.ID);
        this.showPerTurn = false;
    }

    public static HeartBlessingBlueInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = HeartBlessingBlue.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAdjustmentAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(HeartBlessingBlue.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = PreAdjustmentAction.fromAdjustment(getInstance(), () -> TempHPField.tempHp.get(AbstractDungeon.player)));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAdjustmentAction(preAction));
        }
    }
}
