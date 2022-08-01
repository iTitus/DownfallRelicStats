package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import relicstats.AmountIncreaseCallback;

public class AoePowerFollowupAction extends AbstractGameAction {

    private final AmountIncreaseCallback statTracker;
    private final PreAoePowerAction preAction;
    private final boolean checkIncrease;

    public AoePowerFollowupAction(AmountIncreaseCallback statTracker, PreAoePowerAction preAction, boolean checkIncrease) {
        this.statTracker = statTracker;
        this.preAction = preAction;
        this.checkIncrease = checkIncrease;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        int newPowerAmount = preAction.countPowerAmount();
        int change = checkIncrease ? newPowerAmount - preAction.getPowerAmount() : preAction.getPowerAmount() - newPowerAmount;
        statTracker.increaseAmount(change);

        isDone = true;
    }
}
