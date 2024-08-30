package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.BottledCollectible;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class BottledCollectibleInfo extends BaseCardRelicStats {

    private static final BottledCollectibleInfo INSTANCE = new BottledCollectibleInfo();

    private BottledCollectibleInfo() {
        super(BottledCollectible.ID);
        this.onlyShowInRunHistory = true;
    }

    public static BottledCollectibleInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BottledCollectible.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(BottledCollectible __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
