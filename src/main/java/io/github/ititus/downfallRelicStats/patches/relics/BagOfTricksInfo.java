package io.github.ititus.downfallRelicStats.patches.relics;

import collector.relics.BagOfTricks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class BagOfTricksInfo extends BaseCombatRelicStats {

    private static final BagOfTricksInfo INSTANCE = new BagOfTricksInfo();

    private BagOfTricksInfo() {
        super(BagOfTricks.ID);
        this.showPerTurn = false;
    }

    public static BagOfTricksInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BagOfTricks.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Prefix(BagOfTricks __instance) {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void Postfix(BagOfTricks __instance) {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
