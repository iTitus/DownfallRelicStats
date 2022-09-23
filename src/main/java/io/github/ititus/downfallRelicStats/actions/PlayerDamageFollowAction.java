package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.patches.ActualLastDamageField;
import relicstats.AmountIncreaseCallback;

public class PlayerDamageFollowAction extends AbstractGameAction {

    private final AmountIncreaseCallback statTracker;
    private final PrePlayerDamageAction preAction;

    public PlayerDamageFollowAction(AmountIncreaseCallback statTracker, PrePlayerDamageAction preAction) {
        this.statTracker = statTracker;
        this.preAction = preAction;
    }

    @Override
    public void update() {
        AbstractPlayer player = preAction.getPlayer();
        if (player != null) {
            statTracker.increaseAmount(ActualLastDamageField.get(AbstractDungeon.player));
        }

        isDone = true;
    }
}
