package io.github.ititus.downfallRelicStats.relics.snecko;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import sneckomod.SneckoMod;
import sneckomod.actions.MuddleAction;
import sneckomod.actions.MuddleMarkedAction;
import sneckomod.relics.CrystallizedMud;

public final class CrystallizedMudInfo extends BaseCombatRelicStats {

    private static final CrystallizedMudInfo INSTANCE = new CrystallizedMudInfo();

    private CrystallizedMudInfo() {
        super(CrystallizedMud.ID);
    }

    public static CrystallizedMudInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatches({
            @SpirePatch(
                    clz = MuddleAction.class,
                    method = "update"
            ),
            @SpirePatch(
                    clz = MuddleMarkedAction.class,
                    method = "update"
            )
    })
    @SuppressWarnings("unused")
    public static class Patch {

        // TODO: keep in sync with MuddleAction#update and MuddleMarkedAction#update
        public static void Postfix(AbstractGameAction __instance, AbstractCard ___card, boolean ___no3) {
            if (___card.cost >= 0 && !___card.hasTag(SneckoMod.SNEKPROOF) && AbstractDungeon.player.hasRelic(CrystallizedMud.ID) && ___card.costForTurn != 0) {
                getInstance().increaseAmount(1);
            }
        }
    }
}
