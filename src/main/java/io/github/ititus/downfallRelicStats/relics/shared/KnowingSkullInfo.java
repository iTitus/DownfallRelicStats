package io.github.ititus.downfallRelicStats.relics.shared;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import downfall.cards.KnowingSkullWish;
import downfall.relics.KnowingSkull;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMultiMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class KnowingSkullInfo extends BaseRelicStats<KnowingSkullInfo.Stats> {

    private static final KnowingSkullInfo INSTANCE = new KnowingSkullInfo();

    private KnowingSkullInfo() {
        super(KnowingSkull.ID, Stats.class);
    }

    public static KnowingSkullInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int hp;
        int gold;
        int goldTimes;
        int cards;
        int potions;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#%");
            int total = goldTimes + cards + potions;
            return description[0] + total +
                    description[1] + hp +
                    description[2] + gold + " (" + df.format((double) goldTimes / Math.max(1, total)) + ")" +
                    description[3] + cards + " (" + df.format((double) cards / Math.max(1, total)) + ")" +
                    description[4] + potions + " (" + df.format((double) potions / Math.max(1, total)) + ")";
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            int total = goldTimes + cards + potions;
            return BaseCombatRelicStats.generateExtendedDescription(description, 5, total, 0, totalCombats);
        }
    }

    @SpirePatch(
            clz = KnowingSkullWish.class,
            method = "doChoiceStuff"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAdjustmentAction preHpAction;
        private static PreAdjustmentAction preGoldAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMultiMethodCallEditor(KnowingSkullWish.class, "atb", Patch.class);
        }

        public static void before(int n) {
            if (n == 0 || n == 2 || n == 4) {
                AbstractDungeon.actionManager.addToBottom(preHpAction = PreAdjustmentAction.fromIncrease(i -> getInstance().stats.hp -= i, () -> AbstractDungeon.player.currentHealth));
            } else if (n == 1) {
                AbstractDungeon.actionManager.addToBottom(preGoldAction = PreAdjustmentAction.fromIncrease(i -> getInstance().stats.gold += i, () -> AbstractDungeon.player.gold));
            } else if (n != 3 && n != 5) {
                throw new AssertionError();
            }
        }

        public static void after(int n) {
            if (n == 0 || n == 2 || n == 4) {
                AbstractDungeon.actionManager.addToBottom(preHpAction.post());
            } else if (n == 1) {
                getInstance().stats.goldTimes++;
                AbstractDungeon.actionManager.addToBottom(preGoldAction.post());
            } else if (n == 3) {
                getInstance().stats.cards++;
            } else if (n == 5) {
                getInstance().stats.potions++;
            } else {
                throw new AssertionError();
            }
        }
    }
}
