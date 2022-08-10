package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import guardian.relics.BottledAnomaly;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class BottledAnomalyInfo extends BaseCardRelicStats {

    private static final BottledAnomalyInfo INSTANCE = new BottledAnomalyInfo();

    private BottledAnomalyInfo() {
        super(BottledAnomaly.ID);
        this.onlyShowInRunHistory = true;
    }

    public static BottledAnomalyInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BottledAnomaly.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(BottledAnomaly __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
