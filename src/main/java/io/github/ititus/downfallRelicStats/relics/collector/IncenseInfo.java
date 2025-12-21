package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.Incense;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.PreAoeDamageAction;

public final class IncenseInfo extends BaseCombatRelicStats {

    private static final IncenseInfo INSTANCE = new IncenseInfo();

    private IncenseInfo() {
        super(Incense.ID);
    }

    public static IncenseInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Incense.class,
            method = "onReceivePower"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(Incense.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            // TODO: show separate info for each debuff
            getInstance().trigger();
        }
    }
}
