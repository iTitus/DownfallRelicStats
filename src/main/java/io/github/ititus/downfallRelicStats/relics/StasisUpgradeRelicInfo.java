package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import guardian.orbs.StasisOrb;
import guardian.relics.StasisUpgradeRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.actions.PostAdjustmentAction;
import io.github.ititus.downfallRelicStats.actions.PreAdjustmentAction;
import io.github.ititus.downfallRelicStats.patches.editor.SafeExprEditor;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.text.DecimalFormat;

public final class StasisUpgradeRelicInfo extends BaseRelicStats<StasisUpgradeRelicInfo.Stats> {

    private static final StasisUpgradeRelicInfo INSTANCE = new StasisUpgradeRelicInfo();

    private StasisUpgradeRelicInfo() {
        super(StasisUpgradeRelic.ID, Stats.class);
    }

    public static StasisUpgradeRelicInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int upgrades = 0;
        int stasis = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + upgrades +
                    description[1] + df.format((double) upgrades / Math.max(1, stasis));
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(extendedDescription, 0, upgrades, totalTurns, totalCombats);
        }
    }

    @SpirePatch(
            clz = StasisOrb.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { AbstractCard.class, CardGroup.class, boolean.class }
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAdjustmentAction preAction;

        public static ExprEditor Instrument() {
            return new SafeExprEditor() {

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(GameActionManager.class.getName()) && m.getMethodName().equals("addToBottom")) {
                        m.replace("{" + Patch.class.getName() + ".before(card);$_=$proceed($$);" + Patch.class.getName() + ".after();}");
                    }
                }
            };
        }

        public static void before(AbstractCard card) {
            AbstractDungeon.actionManager.addToBottom(preAction = PreAdjustmentAction.fromIncrease(n -> getInstance().stats.upgrades += n, () -> card.timesUpgraded));
            getInstance().stats.stasis++;
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAdjustmentAction(preAction));
        }
    }
}
