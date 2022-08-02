package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.GremlinKnob;
import gremlin.relics.GremlinKnobUpgrade;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class GremlinKnobUpgradeInfo extends BaseRelicStats<GremlinKnobInfo.Stats> implements AmountAdjustmentCallback {

    private static final GremlinKnobUpgradeInfo INSTANCE = new GremlinKnobUpgradeInfo();

    private int startingAmount;

    private GremlinKnobUpgradeInfo() {
        super(GremlinKnobUpgrade.ID, GremlinKnobInfo.Stats.class);
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

        public static void before(GremlinKnobUpgrade __instance) {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void after(GremlinKnobUpgrade __instance) {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
