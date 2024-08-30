package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;
import sneckomod.relics.SneckoSoul;

public final class SneckoSoulInfo extends BaseRelicStats<GremlinKnobInfo.Stats> implements AmountAdjustmentCallback {

    private static final SneckoSoulInfo INSTANCE = new SneckoSoulInfo();

    private int startingAmount;

    private SneckoSoulInfo() {
        super(SneckoSoul.ID, GremlinKnobInfo.Stats.class);
    }

    public static SneckoSoulInfo getInstance() {
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
            clz = SneckoSoul.class,
            method = "onUseCard"
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
            clz = SneckoSoul.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(SneckoSoul.class, "addToBot", Patch2.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
