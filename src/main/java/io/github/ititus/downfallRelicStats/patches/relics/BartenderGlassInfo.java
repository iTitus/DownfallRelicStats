package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import hermit.relics.BartenderGlass;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class BartenderGlassInfo extends BaseCombatRelicStats {

    private static final BartenderGlassInfo INSTANCE = new BartenderGlassInfo();

    private BartenderGlassInfo() {
        super(BartenderGlass.ID);
        this.showPerTurn = false;
    }

    public static BartenderGlassInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BartenderGlass.class,
            method = "onUsePotion"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(BartenderGlass.class, "addToBot", Patch.class, true, false);
        }

        public static void before() {
            getInstance().increaseAmount(1);
        }
    }
}
