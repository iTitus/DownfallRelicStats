package io.github.ititus.downfallRelicStats.relics.hermit;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.DentedPlate;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import io.github.ititus.downfallRelicStats.stats.EnergyCardsStats;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class DentedPlateInfo extends BaseRelicStats<EnergyCardsStats> {

    private static final DentedPlateInfo INSTANCE = new DentedPlateInfo();

    private DentedPlateInfo() {
        super(DentedPlate.ID, EnergyCardsStats.class);
    }

    public static DentedPlateInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DentedPlate.class,
            method = "atTurnStartPostDraw"
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
            clz = DentedPlate.class,
            method = "atTurnStartPostDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        private static AmountAdjustmentCallback cardsAdjuster;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, DentedPlate.class, "addToBot", Patch2.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(cardsAdjuster = getInstance().stats.new CardsAdjuster()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(cardsAdjuster));
        }
    }
}
