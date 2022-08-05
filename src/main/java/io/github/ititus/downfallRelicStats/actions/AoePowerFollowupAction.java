package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AoePowerFollowupAction extends AbstractGameAction {

    private final PowerChangeCallback statTracker;
    private final PreAoePowerAction preAction;

    public AoePowerFollowupAction(PowerChangeCallback statTracker, PreAoePowerAction preAction) {
        this.statTracker = statTracker;
        this.preAction = preAction;
    }

    public void update() {
        if (!preAction.isDone) {
            throw new IllegalStateException();
        }

        Map<String, Integer> oldPowerAmounts = preAction.getPowerAmounts();
        Map<String, Integer> newPowerAmounts = preAction.countPowerAmounts();

        Set<String> powerIds = new HashSet<>();
        powerIds.addAll(oldPowerAmounts.keySet());
        powerIds.addAll(newPowerAmounts.keySet());
        for (String powerId : powerIds) {
            int oldAmount = oldPowerAmounts.getOrDefault(powerId, 0);
            int newAmount = newPowerAmounts.getOrDefault(powerId, 0);
            statTracker.onPowerChanged(powerId, newAmount - oldAmount);
        }

        isDone = true;
    }
}
