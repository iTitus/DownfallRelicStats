package io.github.ititus.downfallRelicStats.actions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PostAoePowerAction extends PostAoeAction<PreAoePowerAction> {

    public PostAoePowerAction(PreAoePowerAction preAction) {
        super(preAction);
    }

    @Override
    protected void post() {
        Map<String, Integer> oldPowerAmounts = this.preAction.getPowerAmounts();
        Map<String, Integer> newPowerAmounts = this.preAction.countPowerAmounts();

        Set<String> powerIds = new HashSet<>();
        powerIds.addAll(oldPowerAmounts.keySet());
        powerIds.addAll(newPowerAmounts.keySet());
        for (String powerId : powerIds) {
            int oldAmount = oldPowerAmounts.getOrDefault(powerId, 0);
            int newAmount = newPowerAmounts.getOrDefault(powerId, 0);
            this.preAction.getStatTracker().onPowerChanged(powerId, newAmount - oldAmount);
        }
    }
}
