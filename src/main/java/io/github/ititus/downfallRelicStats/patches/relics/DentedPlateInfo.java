package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.DentedPlate;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class DentedPlateInfo extends BaseRelicStats<GremlinKnobUpgradeInfo.Stats> implements AmountAdjustmentCallback {

    private static final DentedPlateInfo INSTANCE = new DentedPlateInfo();

    private int startingAmount;

    private DentedPlateInfo() {
        super(DentedPlate.ID, GremlinKnobUpgradeInfo.Stats.class);
    }

    public static DentedPlateInfo getInstance() {
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

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, DentedPlate.class, "addToBot", Patch2.class);
        }

        public static void before(DentedPlate __instance) {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void after(DentedPlate __instance) {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
