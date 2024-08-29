package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.GremlinKnobUpgrade;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class GremlinKnobUpgradeInfo extends BaseRelicStats<GremlinKnobUpgradeInfo.Stats> implements AmountAdjustmentCallback {

    private static final GremlinKnobUpgradeInfo INSTANCE = new GremlinKnobUpgradeInfo();

    private int startingAmount;

    private GremlinKnobUpgradeInfo() {
        super(GremlinKnobUpgrade.ID, Stats.class);
    }

    public static GremlinKnobUpgradeInfo getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerStartingAmount(int startingAmount) {
        this.startingAmount = startingAmount;
    }

    @Override
    public void registerEndingAmount(int endingAmount) {
        stats.cards += endingAmount - startingAmount;
    }

    public static class Stats implements StatContainer {

        int energy = 0;
        int cards = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + energy +
                    description[1] + cards;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(description, 2, energy, totalTurns, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 4, cards, totalTurns, totalCombats);
        }
    }

    @SpirePatch(
            clz = GremlinKnobUpgrade.class,
            method = "onShuffle"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainEnergyAction.class, Patch1.class, 1);
        }

        public static void hook(int energyAmount) {
            getInstance().stats.energy += energyAmount;
        }
    }

    @SpirePatch(
            clz = GremlinKnobUpgrade.class,
            method = "onShuffle"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(2, GameActionManager.class, "addToBottom", Patch2.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
