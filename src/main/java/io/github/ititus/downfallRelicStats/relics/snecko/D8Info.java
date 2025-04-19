package io.github.ititus.downfallRelicStats.relics.snecko;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import sneckomod.relics.D8;

public final class D8Info extends BaseRelicStats<D8Info.Stats> {

    private static final D8Info INSTANCE = new D8Info();

    private D8Info() {
        super(D8.ID, Stats.class);
    }

    public static D8Info getInstance() {
        return INSTANCE;
    }

    public static class Stats extends BaseCardRelicStats.Stats {

        int block = 0;

        @Override
        public String getDescription(String[] description) {
            return (CardCrawlGame.isInARun() ? "" : super.getDescription(description) + description[2]) +
                    description[3] + block;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(extendedDescription, 0, block, totalTurns, totalCombats);
        }
    }

    @SpirePatch(
            clz = D8.class,
            method = "setDescriptionAfterLoading"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Postfix(D8 __instance, AbstractCard ___card) {
            getInstance().stats.setCard(___card);
        }
    }

    @SpirePatch(
            clz = D8.class,
            method = "onPlayCard"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch2.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().stats.block += blockAmount;
        }
    }
}
