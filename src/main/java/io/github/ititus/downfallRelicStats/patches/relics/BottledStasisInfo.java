package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import guardian.relics.BottledStasis;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class BottledStasisInfo extends BaseCardRelicStats {

    private static final BottledStasisInfo INSTANCE = new BottledStasisInfo();

    private BottledStasisInfo() {
        super(BottledStasis.ID);
        this.onlyShowInRunHistory = true;
    }

    public static BottledStasisInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BottledStasis.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(BottledStasis __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
