package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import sneckomod.SneckoMod;
import sneckomod.actions.MuddleAction;
import sneckomod.relics.CleanMud;

public final class CleanMudInfo extends BaseCombatRelicStats {

    private static final CleanMudInfo INSTANCE = new CleanMudInfo();

    private CleanMudInfo() {
        super(CleanMud.ID);
    }

    public static CleanMudInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MuddleAction.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        // TODO: keep in sync with MuddleAction#update
        public static void Postfix(MuddleAction __instance, AbstractCard ___card, boolean ___no3) {
            if (___card.cost >= 0 && !___card.hasTag(SneckoMod.SNEKPROOF) && AbstractDungeon.player.hasRelic(CleanMud.ID) && !___no3 && ___card.costForTurn != 3) {
                getInstance().increaseAmount(1);
            }
        }
    }
}
