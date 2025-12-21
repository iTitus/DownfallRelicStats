package io.github.ititus.downfallRelicStats.relics.champ;

import champ.ChampMod;
import champ.relics.ChampionCrownUpgraded;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class ChampionCrownUpgradedInfo extends BaseRelicStats<ChampionCrownUpgradedInfo.Stats> {

    private static final ChampionCrownUpgradedInfo INSTANCE = new ChampionCrownUpgradedInfo();

    private ChampionCrownUpgradedInfo() {
        super(ChampionCrownUpgraded.ID, Stats.class);
    }

    public static ChampionCrownUpgradedInfo getInstance() {
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
            clz = ChampionCrownUpgraded.class,
            method = "onAfterUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ChampMod.class, "berserkOpen", Patch1.class, false, true);
        }

        public static void after() {
            getInstance().stats.berserker++;
        }
    }

    @SpirePatch(
            clz = ChampionCrownUpgraded.class,
            method = "onAfterUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ChampMod.class, "defenseOpen", Patch2.class, false, true);
        }

        public static void after() {
            getInstance().stats.defensive++;
        }
    }
}
