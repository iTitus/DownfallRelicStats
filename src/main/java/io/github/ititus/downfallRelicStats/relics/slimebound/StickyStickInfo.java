package io.github.ititus.downfallRelicStats.relics.slimebound;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import io.github.ititus.downfallRelicStats.stats.EnergyCardsStats;
import javassist.expr.ExprEditor;
import relicstats.AmountAdjustmentCallback;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;
import slimebound.relics.StickyStick;

public final class StickyStickInfo extends BaseRelicStats<EnergyCardsStats> {

    private static final StickyStickInfo INSTANCE = new StickyStickInfo();

    private StickyStickInfo() {
        super(StickyStick.ID, EnergyCardsStats.class);
    }

    public static StickyStickInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = StickyStick.class,
            method = "onCardDraw"
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
            clz = StickyStick.class,
            method = "onCardDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        private static AmountAdjustmentCallback cardsAdjuster;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(StickyStick.class, "addToTop", Patch2.class);
        }

        public static void before() {
            cardsAdjuster = getInstance().stats.new CardsAdjuster();
            AbstractDungeon.actionManager.addToTop(new CardDrawFollowupAction(cardsAdjuster));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToTop(new PreCardDrawAction(cardsAdjuster));
        }
    }
}
