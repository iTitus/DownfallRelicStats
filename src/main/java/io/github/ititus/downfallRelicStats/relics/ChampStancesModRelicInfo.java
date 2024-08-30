package io.github.ititus.downfallRelicStats.relics;

import champ.relics.ChampStancesModRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMultiMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class ChampStancesModRelicInfo extends BaseRelicStats<ChampStancesModRelicInfo.Stats> {

    private static final ChampStancesModRelicInfo INSTANCE = new ChampStancesModRelicInfo();

    private ChampStancesModRelicInfo() {
        super(ChampStancesModRelic.ID, Stats.class);
    }

    public static ChampStancesModRelicInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int defensive = 0;
        int berserker = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + defensive +
                    description[1] + berserker;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            DecimalFormat df = new DecimalFormat("#%");
            int total = Math.max(1, defensive + berserker);
            return description[2] + df.format((double) defensive / total) +
                    description[3] + df.format((double) berserker / total);
        }
    }

    @SpirePatch(
            clz = ChampStancesModRelic.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMultiMethodCallEditor(ChampStancesModRelic.class, "addToBot", Patch.class, false, true);
        }

        public static void after(int index) {
            if (index == 0) {
                getInstance().stats.berserker++;
            } else if (index == 1) {
                getInstance().stats.defensive++;
            } else {
                throw new AssertionError();
            }
        }
    }
}
