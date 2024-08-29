package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import sneckomod.relics.UnknownEgg;
import sneckomod.ui.LockInCampfireOption;

import java.text.DecimalFormat;
import java.util.ArrayList;

public final class UnknownEggInfo extends BaseRelicStats<UnknownEggInfo.Stats> {

    private static final UnknownEggInfo INSTANCE = new UnknownEggInfo();

    private UnknownEggInfo() {
        super(UnknownEgg.ID, Stats.class);
    }

    public static UnknownEggInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int identify = 0;
        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + identify +
                    description[1] + df.format((double) identify / Math.max(1, restSites));
        }
    }

    @SpirePatch(
            clz = LockInCampfireOption.class,
            method = "useOption"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ArrayList.class, "add", Patch1.class, false, true);
        }

        public static void after() {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.hasRelic(UnknownEgg.ID)) {
                getInstance().stats.identify++;
            }
        }
    }

    @SpirePatch(
            clz = LockInCampfireOption.class,
            method = SpirePatch.CONSTRUCTOR
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static void Postfix() {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.hasRelic(UnknownEgg.ID)) {
                getInstance().stats.restSites++;
            }
        }
    }
}
