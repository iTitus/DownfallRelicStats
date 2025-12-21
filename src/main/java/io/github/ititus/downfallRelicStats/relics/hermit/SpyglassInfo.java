package io.github.ititus.downfallRelicStats.relics.hermit;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hermit.powers.Concentration;
import hermit.relics.Spyglass;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class SpyglassInfo extends BaseCombatRelicStats {

    private static final SpyglassInfo INSTANCE = new SpyglassInfo();

    private SpyglassInfo() {
        super(Spyglass.ID);
    }

    public static SpyglassInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Concentration.class,
            method = "atEndOfTurn"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
