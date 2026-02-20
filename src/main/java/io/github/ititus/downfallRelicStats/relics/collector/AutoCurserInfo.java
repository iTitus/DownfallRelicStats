package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.AutoCurser;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;

public final class AutoCurserInfo extends BaseRelicStats<AutoCurserInfo.Stats> {

    private static final AutoCurserInfo INSTANCE = new AutoCurserInfo();

    private AutoCurserInfo() {
        super(AutoCurser.ID, Stats.class);
    }

    public static AutoCurserInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int weak = 0;
        int vulnerable = 0;
        int artifact = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + weak +
                    description[1] + vulnerable +
                    description[2] + artifact;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(description, 3, weak, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 4, vulnerable, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 5, artifact, 0, totalCombats);
        }
    }

    @SpirePatch(
            clz = AutoCurser.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static void Prefix() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction((powerId, by) -> {
                if (WeakPower.POWER_ID.equals(powerId)) {
                    getInstance().stats.weak += by;
                } else if (VulnerablePower.POWER_ID.equals(powerId)) {
                    getInstance().stats.vulnerable += by;
                } else if (ArtifactPower.POWER_ID.equals(powerId)) {
                    getInstance().stats.artifact -= by;
                }
            }));
        }

        public static void Postfix() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
