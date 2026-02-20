package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PreAoeAction<T> extends AbstractGameAction {

    protected final List<AbstractCreature> affectedCreatures = new ArrayList<>();
    protected final PreAoeAction.Mode mode;
    protected final T statTracker;

    public PreAoeAction(T statTracker) {
        this(PreAoeAction.Mode.ONLY_MONSTERS, statTracker);
    }

    public PreAoeAction(PreAoeAction.Mode mode, T statTracker) {
        this.mode = Objects.requireNonNull(mode, "mode");
        this.statTracker = Objects.requireNonNull(statTracker, "statTracker");
        this.actionType = ActionType.SPECIAL;
    }

    public abstract AbstractGameAction post();

    @Override
    public void update() {
        this.affectedCreatures.clear();

        List<AbstractCreature> candidates = new ArrayList<>();
        if (mode == PreAoeAction.Mode.ONLY_MONSTERS || mode == PreAoeAction.Mode.ALL) {
            candidates.addAll(AbstractDungeon.getMonsters().monsters);
        }

        if (mode == PreAoeAction.Mode.ONLY_PLAYER || mode == PreAoeAction.Mode.ALL) {
            candidates.add(AbstractDungeon.player);
        }

        for (AbstractCreature c : candidates) {
            if (c != null && !c.isDead && !c.isDeadOrEscaped() && c.currentHealth > 0) {
                this.affectedCreatures.add(c);
            }
        }

        this.pre();
        isDone = true;
    }

    protected abstract void pre();

    public List<AbstractCreature> getAffectedCreatures() {
        return this.affectedCreatures;
    }

    public T getStatTracker() {
        return statTracker;
    }

    public enum Mode {
        ONLY_MONSTERS,
        ONLY_PLAYER,
        ALL
    }
}
