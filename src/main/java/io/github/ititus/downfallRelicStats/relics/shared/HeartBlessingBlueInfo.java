package io.github.ititus.downfallRelicStats.relics.shared;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import downfall.relics.HeartBlessingBlue;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;

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

        public static void Prefix() {
            preAction = PreAdjustmentAction.fromAdjustment(getInstance(), () -> AbstractDungeon.player.currentHealth).doNotCancelOnCombatEnd();
            AbstractDungeon.actionManager.addToTop(preAction.post());
        }

        public static void Postfix() {
            AbstractDungeon.actionManager.addToTop(preAction);
        }
    }
}
