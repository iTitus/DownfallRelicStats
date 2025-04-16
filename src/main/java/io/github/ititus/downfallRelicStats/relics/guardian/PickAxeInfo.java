package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import guardian.relics.PickAxe;
import guardian.rewards.GemReward;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.track.SourceModifier;

import java.text.DecimalFormat;

public final class PickAxeInfo extends BaseRelicStats<PickAxeInfo.Stats> {

    private static final PickAxeInfo INSTANCE = new PickAxeInfo();

    private PickAxeInfo() {
        super(PickAxe.ID, Stats.class);
    }

    public static PickAxeInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats extends BaseMultiCardRelicStats.Stats {

        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            int gemsMined = getCards().size();
            return description[1] + gemsMined +
                    description[2] + df.format((double) gemsMined / Math.max(1, restSites)) +
                    (gemsMined > 0 ? super.getDescription(description) : "");
        }
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "acquireCard"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Postfix(CardRewardScreen __instance, AbstractCard card) {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.hasRelic(PickAxe.ID)) {
                if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
                    SourceModifier mod = SourceModifier.get(card);
                    if (mod != null && mod.getRewardItem() instanceof GemReward) {
                        getInstance().stats.addCard(card);
                    }
                }
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
            if (AbstractDungeon.player.hasRelic(PickAxe.ID)) {
                getInstance().stats.restSites++;
            }
        }
    }
}
