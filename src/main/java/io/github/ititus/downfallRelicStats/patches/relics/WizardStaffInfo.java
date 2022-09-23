package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import gremlin.powers.WizPower;
import gremlin.relics.WizardStaff;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class WizardStaffInfo extends BaseCombatRelicStats {

    private static final WizardStaffInfo INSTANCE = new WizardStaffInfo();

    private WizardStaffInfo() {
        super(WizardStaff.ID);
    }

    public static WizardStaffInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = WizPower.class,
            method = "onInitialApplication"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch1.class, false, true);
        }

        public static void after() {
            getInstance().stats.increaseAmount(WizardStaff.OOMPH);
        }
    }

    @SpirePatch(
            clz = WizPower.class,
            method = "stackPower"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch2.class, false, true);
        }

        public static void after() {
            getInstance().stats.increaseAmount(WizardStaff.OOMPH);
        }
    }
}
