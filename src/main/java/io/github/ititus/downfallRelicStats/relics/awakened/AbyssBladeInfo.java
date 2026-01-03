package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.AbyssBlade;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class AbyssBladeInfo extends BaseCombatRelicStats {

    private static final AbyssBladeInfo INSTANCE = new AbyssBladeInfo();

    private AbyssBladeInfo() {
        super(AbyssBlade.ID);
    }

    public static AbyssBladeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = AbyssBlade.class,
            method = "LoseEnergyAction"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbyssBlade.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
