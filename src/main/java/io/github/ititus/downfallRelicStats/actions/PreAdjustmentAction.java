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
        this.actionType = ActionType.SPECIAL;
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

    public PreAdjustmentAction doNotCancelOnCombatEnd() {
        this.actionType = ActionType.DAMAGE;
        return this;
    }

    public PostAdjustmentAction post() {
        return new PostAdjustmentAction(this);
    }

    @Override
    public void update() {
        this.registerStartingAmount();
        this.isDone = true;
    }

    protected int getValueSafe() {
        try {
            return this.valueSupplier.getAsInt();
        } catch (Exception ignored) {
            return 0;
        }
    }

    protected void registerStartingAmount() {
        this.statTracker.registerStartingAmount(this.getValueSafe());
    }

    protected void registerEndingAmount() {
        this.statTracker.registerEndingAmount(this.getValueSafe());
    }
}
