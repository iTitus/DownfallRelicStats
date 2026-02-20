package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.*;
import java.util.function.Predicate;

public final class PreAoePowerAction extends PreAoeAction<PowerChangeCallback> {

    private final Predicate<AbstractPower> filter;
    private Map<String, Integer> powerAmounts;

    public PreAoePowerAction(PowerChangeCallback statTracker) {
        this(statTracker, p -> true);
    }

    public PreAoePowerAction(PowerChangeCallback statTracker, String... powerIds) {
        this(statTracker, idFilter(powerIds));
    }

    public PreAoePowerAction(PowerChangeCallback statTracker, Predicate<AbstractPower> filter) {
        this(Mode.ONLY_MONSTERS, statTracker, filter);
    }

    public PreAoePowerAction(Mode mode, PowerChangeCallback statTracker, String... powerIds) {
        this(mode, statTracker, idFilter(powerIds));
    }

    public PreAoePowerAction(Mode mode, PowerChangeCallback statTracker, Predicate<AbstractPower> filter) {
        super(mode, statTracker);
        this.filter = Objects.requireNonNull(filter, "filter");
    }

    public static Predicate<AbstractPower> idFilter(String... powerIds) {
        Objects.requireNonNull(powerIds, "powerIds");
        if (powerIds.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<String> powerIdSet = new HashSet<>(Arrays.asList(powerIds));
        return p -> powerIdSet.contains(p.ID);
    }

    @Override
    protected void pre() {
        this.powerAmounts = this.countPowerAmounts();
    }

    @Override
    public PostAoePowerAction post() {
        return new PostAoePowerAction(this);
    }

    Map<String, Integer> getPowerAmounts() {
        return this.powerAmounts;
    }

    Map<String, Integer> countPowerAmounts() {
        Map<String, Integer> powerAmounts = new HashMap<>();
        for (AbstractCreature c : this.getAffectedCreatures()) {
            if (c != null && c.powers != null) {
                for (AbstractPower power : c.powers) {
                    if (power != null && power.ID != null && power.amount != 0 && filter.test(power)) {
                        powerAmounts.merge(power.ID, power.amount, Integer::sum);
                    }
                }
            }
        }

        return powerAmounts;
    }
}
