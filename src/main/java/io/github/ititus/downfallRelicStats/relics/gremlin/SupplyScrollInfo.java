package io.github.ititus.downfallRelicStats.relics.gremlin;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.SupplyScroll;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import io.github.ititus.downfallRelicStats.stats.EnergyCardsStats;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class SupplyScrollInfo extends BaseRelicStats<EnergyCardsStats> {

    private static final SupplyScrollInfo INSTANCE = new SupplyScrollInfo();

    private SupplyScrollInfo() {
        super(SupplyScroll.ID, EnergyCardsStats.class);
    }

    public static SupplyScrollInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SupplyScroll.class,
            method = "atTurnStart"
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
            clz = SupplyScroll.class,
            method = "atTurnStart"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        private static AmountAdjustmentCallback cardsAdjuster;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(2, SupplyScroll.class, "addToBot", Patch2.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(cardsAdjuster = getInstance().stats.new CardsAdjuster()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(cardsAdjuster));
        }
    }
}
