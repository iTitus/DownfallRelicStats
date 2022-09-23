package io.github.ititus.downfallRelicStats.patches.relics;

import champ.relics.SignatureFinisher;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;

public final class SignatureFinisherInfo extends BaseCardRelicStats {

    private static final SignatureFinisherInfo INSTANCE = new SignatureFinisherInfo();

    private SignatureFinisherInfo() {
        super(SignatureFinisher.ID);
        this.onlyShowInRunHistory = true;
    }

    public static SignatureFinisherInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SignatureFinisher.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(SignatureFinisher __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }
}
