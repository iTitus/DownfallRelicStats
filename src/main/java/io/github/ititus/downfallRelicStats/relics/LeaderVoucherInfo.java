package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.characters.GremlinCharacter;
import gremlin.relics.LeaderVoucher;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;

public final class LeaderVoucherInfo extends BaseRelicStats<LeaderVoucherInfo.Stats> {

    private static final LeaderVoucherInfo INSTANCE = new LeaderVoucherInfo();

    private LeaderVoucherInfo() {
        super(LeaderVoucher.ID, Stats.class);
        this.onlyShowInRunHistory = true;
    }

    public static LeaderVoucherInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int gremlinLostIndex = -1;

        @Override
        public String getDescription(String[] description) {
            return description[0] + (gremlinLostIndex < 0 ? description[1] : description[gremlinLostIndex + 1]);
        }
    }

    @SpirePatch(
            clz = LeaderVoucher.class,
            method = "updateEnslavedTooltip"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(LeaderVoucher __instance, int ___gremIndex) {
            if (AbstractDungeon.player instanceof GremlinCharacter && AbstractDungeon.player.hasRelic(LeaderVoucher.ID)) {
                getInstance().stats.gremlinLostIndex = ___gremIndex;
            }
        }
    }
}
