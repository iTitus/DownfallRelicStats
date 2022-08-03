package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import guardian.orbs.StasisOrb;
import guardian.relics.StasisUpgradeRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
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

        private static int timesUpgraded;

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("upgrade")) {
                        m.replace("{" + Patch.class.getName() + ".before($0);$_=$proceed($$);" + Patch.class.getName() + ".after($0);}");
                    }
                }
            };
        }

        public static void before(AbstractCard card) {
            getInstance().stats.stasis++;
            timesUpgraded = card.timesUpgraded;
        }

        public static void after(AbstractCard card) {
            if (card.timesUpgraded > timesUpgraded) {
                getInstance().stats.upgrades++;
            }
        }
    }
}
