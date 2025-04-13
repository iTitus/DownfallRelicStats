package io.github.ititus.downfallRelicStats.relics.automaton;

import automaton.AutomatonMod;
import automaton.patches.StatusReplacePatch;
import automaton.relics.BronzeIdol;
import basemod.interfaces.PostDrawSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class BronzeIdolInfo extends BaseCombatRelicStats implements PostDrawSubscriber {

    private static final BronzeIdolInfo INSTANCE = new BronzeIdolInfo();

    private BronzeIdolInfo() {
        super(BronzeIdol.ID);
    }

    public static BronzeIdolInfo getInstance() {
        return INSTANCE;
    }

    // TODO: Keep this in sync with AutomatonMod#receivePostDraw
    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        if (abstractCard.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasRelic(BronzeIdol.ID) && !abstractCard.hasTag(AutomatonMod.GOOD_STATUS)) {
            trigger();
        }
    }

    @SpirePatch(
            clz = StatusReplacePatch.class,
            method = "Prefix"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(SpireReturn.class, "Return", Patch.class, true, false);
        }

        public static void before() {
            getInstance().trigger();
        }
    }
}
