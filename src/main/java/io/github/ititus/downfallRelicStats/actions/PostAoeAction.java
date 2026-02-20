package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.Objects;

public abstract class PostAoeAction<T extends PreAoeAction> extends AbstractGameAction {

    protected final T preAction;

    public PostAoeAction(T preAction) {
        this.preAction = Objects.requireNonNull(preAction, "preAction");
        this.actionType = this.preAction.actionType;
    }

    @Override
    public void update() {
        if (!this.preAction.isDone) {
            throw new IllegalStateException();
        }

        this.post();
        this.isDone = true;
    }

    protected abstract void post();

}
