package io.github.ititus.downfallRelicStats.relics.snecko;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import sneckomod.relics.ConfusingCodex;

public final class ConfusingCodexInfo extends BaseRelicStats<ConfusingCodexInfo.Stats> {

    private static final ConfusingCodexInfo INSTANCE = new ConfusingCodexInfo();

    private ConfusingCodexInfo() {
        super(ConfusingCodex.ID, Stats.class);
    }

    public static ConfusingCodexInfo getInstance() {
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
            clz = ConfusingCodex.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static void Prefix() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction());
        }

        public static void Postfix() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction((powerId, by) -> {
                if (WeakPower.POWER_ID.equals(powerId)) {
                    getInstance().stats.weak += by;
                } else if (VulnerablePower.POWER_ID.equals(powerId)) {
                    getInstance().stats.vulnerable += by;
                } else if (ArtifactPower.POWER_ID.equals(powerId)) {
                    getInstance().stats.artifact -= by;
                }
            }, preAction));
        }
    }
}
