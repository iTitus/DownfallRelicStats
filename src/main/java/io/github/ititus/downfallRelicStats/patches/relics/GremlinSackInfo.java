package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import downfall.relics.GremlinSack;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class GremlinSackInfo extends BaseMultiCardRelicStats {

    private static final GremlinSackInfo INSTANCE = new GremlinSackInfo();

    private GremlinSackInfo() {
        super(GremlinSack.ID);
        this.onlyShowInRunHistory = true;
    }

    public static GremlinSackInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = GremlinSack.class,
            method = "onEquip"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(ShowCardAndObtainEffect.class, Patch.class, 1);
        }

        public static void hook(AbstractCard card) {
            getInstance().stats.addCard(card);
        }
    }
}
