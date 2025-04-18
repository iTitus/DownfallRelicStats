package io.github.ititus.downfallRelicStats.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.BolsterEngine;

public final class BolsterEngineInfo extends BaseRelicStats<BolsterEngineInfo.Stats> {

    private static final BolsterEngineInfo INSTANCE = new BolsterEngineInfo();

    private BolsterEngineInfo() {
        super(BolsterEngine.ID, Stats.class);
    }

    public static BolsterEngineInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int block = 0;
        int strength = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + block +
                    description[1] + strength;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            // do not show per turn
            return BaseCombatRelicStats.generateExtendedDescription(description, 2, block, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 3, strength, 0, totalCombats);
        }
    }

    @SpirePatch(
            clz = BolsterEngine.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch1.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().stats.block += blockAmount;
        }
    }

    @SpirePatch(
            clz = BolsterEngine.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(2, BolsterEngine.class, "addToBot", Patch2.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_PLAYER, StrengthPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction((powerId, amount) -> getInstance().stats.strength += amount, preAction));
        }
    }
}
