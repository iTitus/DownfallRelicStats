package io.github.ititus.downfallRelicStats.relics.slimebound;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import slimebound.relics.GreedOozeRelic;

import java.text.DecimalFormat;

public final class GreedOozeRelicInfo extends BaseRelicStats<GreedOozeRelicInfo.Stats> {

    private static final GreedOozeRelicInfo INSTANCE = new GreedOozeRelicInfo();
    private static final int STARTING_DAMAGE = 4;

    private GreedOozeRelicInfo() {
        super(GreedOozeRelic.ID, Stats.class);
    }

    public static GreedOozeRelicInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int damage = STARTING_DAMAGE;
        int upgradeAmount = 0;
        int goldSpent = 0;
        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + damage +
                    description[1] + upgradeAmount +
                    description[2] + df.format((double) upgradeAmount / Math.max(1, restSites)) +
                    description[3] + goldSpent;
        }
    }

    @SpirePatch(
            clz = GreedOozeRelic.class,
            method = "onEnterRestRoom"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static int goldAmount;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "loseGold", Patch.class, true, true).addThis();
        }

        public static void Prefix() {
            getInstance().stats.restSites++;
        }

        public static void before(GreedOozeRelic __instance) {
            goldAmount = AbstractDungeon.player.gold;
        }

        public static void after(GreedOozeRelic __instance) {
            getInstance().stats.goldSpent += goldAmount - AbstractDungeon.player.gold;
            getInstance().stats.upgradeAmount++;
            getInstance().stats.damage = __instance.counter;
        }
    }
}
