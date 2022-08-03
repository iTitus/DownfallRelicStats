package io.github.ititus.downfallRelicStats.patches.relics;

import champ.relics.GladiatorsBookOfMartialProwess;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class GladiatorsBookOfMartialProwessInfo extends BaseCombatRelicStats {

    private static final GladiatorsBookOfMartialProwessInfo INSTANCE = new GladiatorsBookOfMartialProwessInfo();

    private GladiatorsBookOfMartialProwessInfo() {
        super(GladiatorsBookOfMartialProwess.ID);
    }

    public static GladiatorsBookOfMartialProwessInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = GladiatorsBookOfMartialProwess.class,
            method = "onPlayCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, GladiatorsBookOfMartialProwess.class, "addToBot", Patch.class, false, true, false);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
