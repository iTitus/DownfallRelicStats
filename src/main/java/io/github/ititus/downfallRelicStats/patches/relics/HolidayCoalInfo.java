package io.github.ititus.downfallRelicStats.patches.relics;

import collector.actions.DrawAndRemoveMegatherealFromCollectedCardAction;
import collector.actions.DrawCardFromCollectionAction;
import collector.relics.HolidayCoal;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class HolidayCoalInfo extends BaseCombatRelicStats {

    private static final HolidayCoalInfo INSTANCE = new HolidayCoalInfo();

    private HolidayCoalInfo() {
        super(HolidayCoal.ID);
    }

    public static HolidayCoalInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DrawCardFromCollectionAction.class,
            method = "update"
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
            clz = DrawAndRemoveMegatherealFromCollectedCardAction.class,
            method = "update"
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
