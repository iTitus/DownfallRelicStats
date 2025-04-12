package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import guardian.relics.PickAxe;
import guardian.ui.FindGemsOption;
import guardian.vfx.CampfireFindGemsEffect;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class PickAxeInfo extends BaseRelicStats<PickAxeInfo.Stats> {

    private static final PickAxeInfo INSTANCE = new PickAxeInfo();

    private PickAxeInfo() {
        super(PickAxe.ID, Stats.class);
    }

    public static PickAxeInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats extends BaseMultiCardRelicStats.Stats {

        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            int gemsMined = getCards().size();
            return description[1] + gemsMined +
                    description[2] + df.format((double) gemsMined / Math.max(1, restSites)) +
                    (gemsMined > 0 ? super.getDescription(description) : "");
        }
    }

    /*TODO: @SpirePatch(
            clz = CampfireFindGemsEffect.class,
            method = "update"
    )*/
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(ShowCardAndObtainEffect.class, Patch1.class, 1);
        }

        public static void hook(AbstractCard gemCard) {
            getInstance().stats.addCard(gemCard);
        }
    }

    @SpirePatch(
            clz = FindGemsOption.class,
            method = SpirePatch.CONSTRUCTOR
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static void Postfix() {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.hasRelic(PickAxe.ID)) {
                getInstance().stats.restSites++;
            }
        }
    }
}
