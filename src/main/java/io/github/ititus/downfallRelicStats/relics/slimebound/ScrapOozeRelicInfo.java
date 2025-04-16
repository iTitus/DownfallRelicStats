package io.github.ititus.downfallRelicStats.relics.slimebound;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import slimebound.relics.ScrapOozeRelic;

import java.text.DecimalFormat;

public final class ScrapOozeRelicInfo extends BaseRelicStats<ScrapOozeRelicInfo.Stats> {

    private static final ScrapOozeRelicInfo INSTANCE = new ScrapOozeRelicInfo();
    private static final int STARTING_DAMAGE = 6;

    private ScrapOozeRelicInfo() {
        super(ScrapOozeRelic.ID, Stats.class);
    }

    public static ScrapOozeRelicInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int damage = STARTING_DAMAGE;
        int rare = 0;
        int uncommon = 0;
        int common = 0;
        int curse = 0;
        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            int upgradeAmount = rare + uncommon + common + curse;
            return description[0] + damage +
                    description[1] + upgradeAmount +
                    description[2] + df.format((double) upgradeAmount / Math.max(1, restSites)) +
                    (rare > 0 ? description[3] + rare : "") +
                    (uncommon > 0 ? description[4] + uncommon : "") +
                    (common > 0 ? description[5] + common : "") +
                    (curse > 0 ? description[6] + curse : "");
        }
    }

    @SpirePatch(
            clz = ScrapOozeRelic.class,
            method = "incrementScrapNum"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Postfix(ScrapOozeRelic __instance, int amount) {
            getInstance().stats.damage = __instance.counter;
            switch (amount) {
                case 2:
                    getInstance().stats.rare++;
                    break;
                case 1:
                    getInstance().stats.uncommon++;
                    break;
                case -1:
                    getInstance().stats.common++;
                    break;
                case -2:
                    getInstance().stats.curse++;
                    break;
                case 0:
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }

    @SpirePatch(
            clz = RestRoom.class,
            method = "onPlayerEntry"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static void Postfix() {
            if (AbstractDungeon.player.hasRelic(ScrapOozeRelic.ID)) {
                getInstance().stats.restSites++;
            }
        }
    }
}
