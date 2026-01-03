package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.MoonTalisman;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class MoonTalismanInfo extends BaseCardRelicStats {

    private static final MoonTalismanInfo INSTANCE = new MoonTalismanInfo();

    private MoonTalismanInfo() {
        super(MoonTalisman.ID);
        this.onlyShowInRunHistory = true;
    }

    public static MoonTalismanInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MoonTalisman.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(MoonTalisman __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
