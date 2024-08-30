package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.ForbiddenFruit;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class ForbiddenFruitInfo extends BaseMultiCardRelicStats {

    private static final ForbiddenFruitInfo INSTANCE = new ForbiddenFruitInfo();

    private ForbiddenFruitInfo() {
        super(ForbiddenFruit.ID);
    }

    public static ForbiddenFruitInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ForbiddenFruit.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(ShowCardAndObtainEffect.class, Patch.class, 1);
        }

        public static void hook(AbstractCard card) {
            if (!(card instanceof Necronomicurse)) {
                getInstance().stats.addCard(card);
            }
        }
    }
}
