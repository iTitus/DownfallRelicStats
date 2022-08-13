package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.*;
import java.util.function.Predicate;

public class PreAoePowerAction extends AbstractGameAction {

    private final List<AbstractCreature> affectedCreatures = new ArrayList<>();
    private final Mode mode;
    private final Predicate<AbstractPower> filter;
    private Map<String, Integer> powerAmounts;

    public PreAoePowerAction() {
        this(Mode.ONLY_MONSTERS);
    }

    public PreAoePowerAction(Mode mode) {
        this(mode, p -> true);
    }

    public PreAoePowerAction(String... powerIds) {
        this(idFilter(powerIds));
    }

    public PreAoePowerAction(Predicate<AbstractPower> filter) {
        this(Mode.ONLY_MONSTERS, filter);
    }

    public PreAoePowerAction(Mode mode, String... powerIds) {
        this(mode, idFilter(powerIds));
    }

    public PreAoePowerAction(Mode mode, Predicate<AbstractPower> filter) {
        this.mode = Objects.requireNonNull(mode, "mode");
        this.filter = Objects.requireNonNull(filter, "filter");
    }

    public static Predicate<AbstractPower> idFilter(String... powerIds) {
        Objects.requireNonNull(powerIds, "powerIds");
        if (powerIds.length == 0) {
            throw new IllegalArgumentException();
        }

        List<String> powerIdList = new ArrayList<>(Arrays.asList(powerIds));
        return p -> powerIdList.stream().anyMatch(id -> p.ID.equals(id));
    }

    @Override
    public void update() {
        if (mode == Mode.ONLY_MONSTERS || mode == Mode.ALL) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDead && !m.isDeadOrEscaped() && m.currentHealth > 0) {
                    affectedCreatures.add(m);
                }
            }
        }

        if (mode == Mode.ONLY_PLAYER || mode == Mode.ALL) {
            AbstractCreature c = AbstractDungeon.player;
            if (c != null && !c.isDead && !c.isDeadOrEscaped() && c.currentHealth > 0) {
                affectedCreatures.add(c);
            }
        }

        powerAmounts = countPowerAmounts();
        isDone = true;
    }

    public List<AbstractCreature> getAffectedCreatures() {
        return affectedCreatures;
    }

    public Map<String, Integer> getPowerAmounts() {
        return powerAmounts;
    }

    Map<String, Integer> countPowerAmounts() {
        Map<String, Integer> powerAmounts = new HashMap<>();
        for (AbstractCreature c : affectedCreatures) {
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

    public enum Mode {
        ONLY_MONSTERS,
        ONLY_PLAYER,
        ALL
    }
}
