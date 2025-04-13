package io.github.ititus.downfallRelicStats.relics.champ;

import champ.relics.Barbells;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class BarbellsInfo extends BaseRelicStats<BarbellsInfo.Stats> {

    private static final BarbellsInfo INSTANCE = new BarbellsInfo();

    private BarbellsInfo() {
        super(Barbells.ID, Stats.class);
    }

    public static BarbellsInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int upgrades = 0;
        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + upgrades +
                    description[1] + df.format((double) upgrades / Math.max(1, restSites));
        }
    }

    @SpirePatch(
            clz = Barbells.class,
            method = "onEnterRoom"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        @SpireInstrumentPatch
        public static ExprEditor Instrument1() {
            return new BeforeAfterMethodCallEditor(Barbells.class, "flash", Patch.class, true, false);
        }

        @SpireInstrumentPatch
        public static ExprEditor Instrument2() {
            return new BeforeAfterMethodCallEditor(AbstractCard.class, "upgrade", Patch.class, false, true);
        }

        public static void before() {
            getInstance().stats.restSites++;
        }

        public static void after() {
            getInstance().stats.upgrades++;
        }
    }
}
