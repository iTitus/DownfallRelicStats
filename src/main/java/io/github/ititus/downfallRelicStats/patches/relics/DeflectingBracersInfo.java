package io.github.ititus.downfallRelicStats.patches.relics;

import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import champ.ChampMod;
import champ.relics.DeflectingBracers;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import guardian.stances.DefensiveMode;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class DeflectingBracersInfo extends BaseCombatRelicStats implements OnPlayerLoseBlockSubscriber {

    private static final DeflectingBracersInfo INSTANCE = new DeflectingBracersInfo();

    private DeflectingBracersInfo() {
        super(DeflectingBracers.ID);
    }

    public static DeflectingBracersInfo getInstance() {
        return INSTANCE;
    }

    // TODO: Keep this in sync with ChampMod#receiveOnPlayerLoseBlock
    @Override
    public int receiveOnPlayerLoseBlock(int blockToExpire) {
        if (AbstractDungeon.player.stance instanceof DefensiveMode) {
            return blockToExpire;
        }

        if (AbstractDungeon.player.hasRelic(DeflectingBracers.ID)) {
            int counter = Math.min(blockToExpire, AbstractDungeon.player.currentBlock / 2);
            if (counter > 0) {
                increaseAmount(counter);
            }
        }

        return blockToExpire;
    }
}
