package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import relicstats.AmountIncreaseCallback;

public final class PreAoeDamageAction extends PreAoeAction<AmountIncreaseCallback> {

    public PreAoeDamageAction(AmountIncreaseCallback statTracker) {
        this(Mode.ONLY_MONSTERS, statTracker);
    }

    public PreAoeDamageAction(Mode mode, AmountIncreaseCallback statTracker) {
        super(mode, statTracker);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
    }

    @Override
    public PostAoeDamageAction post() {
        return new PostAoeDamageAction(this);
    }

    @Override
    protected void pre() {
        for (AbstractCreature c : this.affectedCreatures) {
            c.lastDamageTaken = 0;
        }
    }
}
