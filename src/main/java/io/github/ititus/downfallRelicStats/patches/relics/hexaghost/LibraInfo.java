package io.github.ititus.downfallRelicStats.patches.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import theHexaghost.relics.Libra;

public final class LibraInfo extends BaseMultiCardRelicStats {

    private static final LibraInfo INSTANCE = new LibraInfo();

    private LibraInfo() {
        super(Libra.ID);
    }

    public static LibraInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Libra.class,
            method = "giveCards"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(GridCardSelectScreen.class.getName()) && m.getMethodName().equals("openConfirmationGrid")) {
                        m.replace("{" + Patch.class.getName() + ".hook($1);$_=$proceed($$);}");
                    }
                }
            };
        }

        public static void hook(CardGroup new_cards) {
            for (AbstractCard card : new_cards.group) {
                getInstance().stats.addCard(card);
            }
        }
    }
}
