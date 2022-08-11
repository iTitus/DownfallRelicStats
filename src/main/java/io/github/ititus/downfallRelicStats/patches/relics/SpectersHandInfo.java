package io.github.ititus.downfallRelicStats.patches.relics;

import champ.relics.SpectersHand;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMultiMethodCallEditor;
import io.github.ititus.downfallRelicStats.StatContainer;
import javassist.expr.ExprEditor;

public final class SpectersHandInfo extends BaseRelicStats<SpectersHandInfo.Stats> {

    private static final SpectersHandInfo INSTANCE = new SpectersHandInfo();

    private SpectersHandInfo() {
        super(SpectersHand.ID, Stats.class);
    }

    public static SpectersHandInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int strike = 0;
        int defend = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + strike +
                    description[1] + defend;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(description, 2, strike, totalTurns, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 4, defend, totalTurns, totalCombats);
        }
    }

    @SpirePatch(
            clz = SpectersHand.class,
            method = "onChangeStance"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMultiMethodCallEditor(SpectersHand.class, "addToBot", Patch.class, false, true);
        }

        public static void after(int index) {
            if (index == 0) {
                getInstance().stats.strike++;
            } else if (index == 1) {
                getInstance().stats.defend++;
            } else {
                throw new AssertionError();
            }
        }
    }
}
