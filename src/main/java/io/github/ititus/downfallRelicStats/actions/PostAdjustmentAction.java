package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.Objects;

public class PostAdjustmentAction extends AbstractGameAction {

    private final PreAdjustmentAction preAction;

    public PostAdjustmentAction(PreAdjustmentAction preAction) {
        this.preAction = Objects.requireNonNull(preAction, "preAction");
    }

    public void update() {
        if (!this.preAction.isDone) {
            throw new IllegalStateException();
        }

        this.preAction.registerEndingAmount();
        this.isDone = true;
    }
}
