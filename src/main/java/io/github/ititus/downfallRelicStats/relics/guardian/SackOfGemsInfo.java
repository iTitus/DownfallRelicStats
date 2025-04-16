package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import guardian.relics.PickAxe;
import guardian.relics.SackOfGems;
import guardian.rewards.GemReward;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.patches.track.SourceModifier;
import relicstats.RelicObtainStats;

public final class SackOfGemsInfo extends BaseMultiCardRelicStats {

    private static final SackOfGemsInfo INSTANCE = new SackOfGemsInfo();

    private SackOfGemsInfo() {
        super(SackOfGems.ID);
    }

    public static SackOfGemsInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "acquireCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(CardRewardScreen __instance, AbstractCard card) {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.hasRelic(SackOfGems.ID) && RelicObtainStats.hasRelic(SackOfGems.ID)) {
                if (RelicObtainStats.getFloor(PickAxe.ID) == AbstractDungeon.floorNum) {
                    SourceModifier mod = SourceModifier.get(card);
                    if (mod != null && mod.getRewardItem() instanceof GemReward) {
                        getInstance().stats.addCard(card);
                    }
                }
            }
        }
    }
}
