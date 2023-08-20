package io.github.ititus.downfallRelicStats.patches.relics;

import automaton.relics.Timepiece;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import expansioncontent.cardmods.RetainCardMod;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class TimepieceInfo extends BaseCombatRelicStats {

    private static final TimepieceInfo INSTANCE = new TimepieceInfo();

    private TimepieceInfo() {
        super(Timepiece.ID);
    }

    public static TimepieceInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Timepiece.class,
            method = "receiveCompile"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Prefix(Timepiece __instance, AbstractCard function, boolean forGameplay) {
            if (forGameplay && new RetainCardMod().shouldApply(function)) {
                getInstance().increaseAmount(1);
            }
        }
    }
}
