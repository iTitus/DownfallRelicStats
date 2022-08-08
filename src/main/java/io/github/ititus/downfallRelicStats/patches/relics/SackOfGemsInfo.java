package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import guardian.relics.SackOfGems;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public class SackOfGemsInfo extends BaseMultiCardRelicStats {

    private static final SackOfGemsInfo INSTANCE = new SackOfGemsInfo();

    private SackOfGemsInfo() {
        super(SackOfGems.ID);
    }

    public static SackOfGemsInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SackOfGems.class,
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
