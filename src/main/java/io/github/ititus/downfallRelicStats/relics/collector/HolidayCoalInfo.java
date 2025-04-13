package io.github.ititus.downfallRelicStats.relics.collector;

import collector.actions.DrawAndRemoveMegatherealFromCollectedCardAction;
import collector.actions.DrawCardFromCollectionAction;
import collector.relics.HolidayCoal;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class HolidayCoalInfo extends BaseCombatRelicStats {

    private static final HolidayCoalInfo INSTANCE = new HolidayCoalInfo();

    private HolidayCoalInfo() {
        super(HolidayCoal.ID);
    }

    public static HolidayCoalInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatches({
            @SpirePatch(
                    clz = DrawCardFromCollectionAction.class,
                    method = "update"
            ),
            @SpirePatch(
                    clz = DrawAndRemoveMegatherealFromCollectedCardAction.class,
                    method = "update"
            )
    })
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
