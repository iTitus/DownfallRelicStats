package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import relicstats.AmountAdjustmentCallback;
import relicstats.AmountIncreaseCallback;

import java.util.Objects;
import java.util.function.IntSupplier;

public class PreAdjustmentAction extends AbstractGameAction {

    private final AmountAdjustmentCallback statTracker;
    private final IntSupplier valueSupplier;

    public PreAdjustmentAction(AmountIncreaseCallback statTracker, IntSupplier valueSupplier) {
        this(new AmountAdjustmentCallback() {

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
        Objects.requireNonNull(statTracker, "statTracker");
    }

    public PreAdjustmentAction(AmountAdjustmentCallback statTracker, IntSupplier valueSupplier) {
        this.statTracker = Objects.requireNonNull(statTracker, "statTracker");
        this.valueSupplier = Objects.requireNonNull(valueSupplier, "valueSupplier");
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
