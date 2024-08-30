package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import downfall.patches.ui.campfire.AddWheelSpinButtonPatch;
import downfall.relics.GremlinWheel;
import downfall.ui.campfire.WheelSpinButton;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class GremlinWheelInfo extends BaseRelicStats<GremlinWheelInfo.Stats> {

    private static final GremlinWheelInfo INSTANCE = new GremlinWheelInfo();

    private GremlinWheelInfo() {
        super(GremlinWheel.ID, Stats.class);
    }

    public static GremlinWheelInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int wheelSpins = 0;
        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + wheelSpins +
                    description[1] + df.format((double) wheelSpins / Math.max(1, restSites));
        }
    }

    @SpirePatch(
            clz = WheelSpinButton.class,
            method = "useOption"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Postfix() {
            getInstance().stats.wheelSpins++;
        }
    }

    @SpirePatch(
            clz = AddWheelSpinButtonPatch.AddKeys.class,
            method = "patch"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "getRelic", Patch2.class, true, false);
        }

        public static void before() {
            // TODO: check if other relic stats also count a campfire as two if the gremlin wheel is involved
            // TODO: keep in sync with AddWheelSpinButtonPatch.AddKeys#update
            if (CardCrawlGame.isInARun() && AbstractDungeon.player.hasRelic(GremlinWheel.ID) && !((GremlinWheel) AbstractDungeon.player.getRelic(GremlinWheel.ID)).justFailed) {
                getInstance().stats.restSites++;
            }
        }
    }
}
