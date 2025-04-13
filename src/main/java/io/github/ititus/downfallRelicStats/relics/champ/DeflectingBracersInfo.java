package io.github.ititus.downfallRelicStats.relics.champ;

import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import champ.relics.DeflectingBracers;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Calipers;
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

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        // we cannot patch this because it breaks automatic card registration
        // TODO: keep in sync with ChampMod#receiveOnPlayerLoseBlock
        if (!(AbstractDungeon.player.stance instanceof DefensiveMode) && AbstractDungeon.player.hasRelic(DeflectingBracers.ID)) {
            int counter;
            if (AbstractDungeon.player.hasRelic(Calipers.ID)) {
                counter = Math.min(7, AbstractDungeon.player.currentBlock / 2);
            } else {
                counter = Math.min(i, AbstractDungeon.player.currentBlock / 2);
            }

            if (counter > 0) {
                getInstance().increaseAmount(counter);
            }
        }
        return i;
    }
}
