package io.github.ititus.downfallRelicStats.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class PreAoePowerAction extends AbstractGameAction {

    private final String powerId;
    private final List<AbstractMonster> affectedMonsters = new ArrayList<>();
    private int powerAmount;

    public PreAoePowerAction(String powerId) {
        this.powerId = powerId;
    }

    @Override
    public void update() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDead && !m.isDeadOrEscaped()) {
                affectedMonsters.add(m);
            }
        }

        powerAmount = countPowerAmount();
        isDone = true;
    }

    public String getPowerId() {
        return powerId;
    }

    public List<AbstractMonster> getAffectedMonsters() {
        return affectedMonsters;
    }

    public int getPowerAmount() {
        return powerAmount;
    }

    int countPowerAmount() {
        int powerAmount = 0;
        for (AbstractMonster m : affectedMonsters) {
            AbstractPower power = m.getPower(powerId);
            if (power != null) {
                powerAmount += power.amount;
            }
        }

        return powerAmount;
    }
}
