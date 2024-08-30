package io.github.ititus.downfallRelicStats.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.ghostflames.CrushingGhostflame;
import theHexaghost.ghostflames.SearingGhostflame;
import theHexaghost.relics.JarOfFuel;

public final class JarOfFuelInfo extends BaseCombatRelicStats {

    private static final JarOfFuelInfo INSTANCE = new JarOfFuelInfo();

    private JarOfFuelInfo() {
        super(JarOfFuel.ID);
    }

    public static JarOfFuelInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SearingGhostflame.class,
            method = "advanceTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch1.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }

    @SpirePatch(
            clz = CrushingGhostflame.class,
            method = "advanceTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch2.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
