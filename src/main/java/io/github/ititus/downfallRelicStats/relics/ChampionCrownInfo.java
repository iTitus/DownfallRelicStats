package io.github.ititus.downfallRelicStats.relics;

import champ.relics.ChampionCrown;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMultiMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class ChampionCrownInfo extends BaseRelicStats<ChampionCrownInfo.Stats> {

    private static final ChampionCrownInfo INSTANCE = new ChampionCrownInfo();

    private ChampionCrownInfo() {
        super(ChampionCrown.ID, Stats.class);
    }

    public static ChampionCrownInfo getInstance() {
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
            clz = ChampionCrown.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMultiMethodCallEditor(ChampionCrown.class, "addToBot", Patch.class, false, true);
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
