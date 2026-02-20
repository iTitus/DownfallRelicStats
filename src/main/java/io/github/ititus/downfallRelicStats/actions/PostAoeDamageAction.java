package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.core.AbstractCreature;

public final class PostAoeDamageAction extends PostAoeAction<PreAoeDamageAction> {

    public PostAoeDamageAction(PreAoeDamageAction preAction) {
        super(preAction);
    }

    @Override
    protected void post() {
        for (AbstractCreature c : this.preAction.getAffectedCreatures()) {
            this.preAction.getStatTracker().increaseAmount(c.lastDamageTaken);
        }
    }
}
