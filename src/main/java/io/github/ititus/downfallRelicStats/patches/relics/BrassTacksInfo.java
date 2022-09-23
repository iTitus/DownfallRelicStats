package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import hermit.relics.BrassTacks;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class BrassTacksInfo extends BaseCombatRelicStats {
    private static final BrassTacksInfo INSTANCE = new BrassTacksInfo();
    private static final int BRASS_TACKS_DEFAULT_BLOCK_AMOUNT = 2;

    private BrassTacksInfo() {
        super(BrassTacks.ID);
        this.showPerTurn = false;
    }

    public static BrassTacksInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MetallicizePower.class,
            method = "atEndOfTurnPreEndTurnCards"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix() {
            if (AbstractDungeon.player.hasRelic(BrassTacks.ID)) {
                getInstance().increaseAmount(BRASS_TACKS_DEFAULT_BLOCK_AMOUNT);
            }
        }
    }
}