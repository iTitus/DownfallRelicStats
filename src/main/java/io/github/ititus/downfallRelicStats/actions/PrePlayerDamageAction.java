package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.patches.ActualLastDamageField;

public class PrePlayerDamageAction extends AbstractGameAction {

    private AbstractPlayer player;

    @Override
    public void update() {
        if (AbstractDungeon.player != null && !AbstractDungeon.player.isDead && !AbstractDungeon.player.isDeadOrEscaped() && AbstractDungeon.player.currentHealth > 0) {
            ActualLastDamageField.set(AbstractDungeon.player, 0);
            this.player = AbstractDungeon.player;
        }

        isDone = true;
    }

    public AbstractPlayer getPlayer() {
        return player;
    }
}
