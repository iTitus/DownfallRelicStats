package io.github.ititus.downfallRelicStats.relics.champ;

import champ.relics.FightingForDummies;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class FightingForDummiesInfo extends BaseCombatRelicStats {

    private static final FightingForDummiesInfo INSTANCE = new FightingForDummiesInfo();

    private FightingForDummiesInfo() {
        super(FightingForDummies.ID);
    }

    public static FightingForDummiesInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = FightingForDummies.class,
            method = "atTurnStartPostDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Prefix() {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void Postfix() {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
