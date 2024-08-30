package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import guardian.relics.StasisEgg;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class StasisEggInfo extends BaseCardRelicStats {

    private static final StasisEggInfo INSTANCE = new StasisEggInfo();

    private StasisEggInfo() {
        super(StasisEgg.ID);
        this.onlyShowInRunHistory = true;
    }

    public static StasisEggInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = StasisEgg.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(StasisEgg __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
