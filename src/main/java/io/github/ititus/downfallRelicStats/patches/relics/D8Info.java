package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;
import sneckomod.relics.D8;

public final class D8Info extends BaseCardRelicStats {

    private static final D8Info INSTANCE = new D8Info();

    private D8Info() {
        super(D8.ID);
        this.onlyShowInRunHistory = true;
    }

    public static D8Info getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = D8.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(D8 __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
