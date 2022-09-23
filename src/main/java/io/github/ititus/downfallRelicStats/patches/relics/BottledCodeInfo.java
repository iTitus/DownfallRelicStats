package io.github.ititus.downfallRelicStats.patches.relics;

import automaton.relics.BottledCode;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class BottledCodeInfo extends BaseCardRelicStats {

    private static final BottledCodeInfo INSTANCE = new BottledCodeInfo();

    private BottledCodeInfo() {
        super(BottledCode.ID);
        this.onlyShowInRunHistory = true;
    }

    public static BottledCodeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BottledCode.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(BottledCode __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
