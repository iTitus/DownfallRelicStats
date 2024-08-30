package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import relicstats.AmountAdjustmentCallback;
import relicstats.AmountIncreaseCallback;

import java.util.Objects;
import java.util.function.IntSupplier;

public class PreAdjustmentAction extends AbstractGameAction {

    private final AmountAdjustmentCallback statTracker;
    private final IntSupplier valueSupplier;

    private PreAdjustmentAction(AmountAdjustmentCallback statTracker, IntSupplier valueSupplier) {
        this.statTracker = Objects.requireNonNull(statTracker, "statTracker");
        this.valueSupplier = Objects.requireNonNull(valueSupplier, "valueSupplier");
    }

    public static PreAdjustmentAction fromIncrease(AmountIncreaseCallback statTracker, IntSupplier valueSupplier) {
        return new PreAdjustmentAction(new AmountAdjustmentCallback() {

            int pre;

            @Override
            public void registerStartingAmount(int i) {
                pre = i;
            }

            @Override
            public void registerEndingAmount(int i) {
                statTracker.increaseAmount(i - pre);
            }
        }, valueSupplier);
    }

    public static PreAdjustmentAction fromAdjustment(AmountAdjustmentCallback statTracker, IntSupplier valueSupplier) {
        return new PreAdjustmentAction(statTracker, valueSupplier);
    }

    @Override
    public void update() {
        this.registerStartingAmount();
        this.isDone = true;
    }

    protected void registerStartingAmount() {
        this.statTracker.registerStartingAmount(this.valueSupplier.getAsInt());
    }

    protected void registerEndingAmount() {
        this.statTracker.registerEndingAmount(this.valueSupplier.getAsInt());
    }
}
