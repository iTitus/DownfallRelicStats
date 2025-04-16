package io.github.ititus.downfallRelicStats.relics.gremlin;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import gremlin.characters.GremlinCharacter;
import gremlin.relics.GremlinGravestone;
import gremlin.ui.campfire.ResurrectOption;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class GremlinGravestoneInfo extends BaseRelicStats<GremlinGravestoneInfo.Stats> {

    private static final GremlinGravestoneInfo INSTANCE = new GremlinGravestoneInfo();

    private GremlinGravestoneInfo() {
        super(GremlinGravestone.ID, Stats.class);
    }

    public static GremlinGravestoneInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int resurrects = 0;
        int restSites = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + resurrects +
                    description[1] + df.format((double) resurrects / Math.max(1, restSites));
        }
    }

    @SpirePatch(
            clz = ResurrectOption.class,
            method = "useOption"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GremlinCharacter.class, "resurrect", Patch1.class, false, true);
        }

        public static void after() {
            getInstance().stats.resurrects++;
        }
    }

    @SpirePatch(
            clz = RestRoom.class,
            method = "onPlayerEntry"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static void Postfix() {
            if (AbstractDungeon.player.hasRelic(GremlinGravestone.ID)) {
                getInstance().stats.restSites++;
            }
        }
    }
}
