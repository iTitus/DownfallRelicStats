package io.github.ititus.downfallRelicStats.relics.champ;

import champ.relics.DefensiveTrainingManual;
import champ.stances.DefensiveStance;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class DefensiveTrainingManualInfo extends BaseCombatRelicStats {

    private static final DefensiveTrainingManualInfo INSTANCE = new DefensiveTrainingManualInfo();

    private DefensiveTrainingManualInfo() {
        super(DefensiveTrainingManual.ID);
    }

    public static DefensiveTrainingManualInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DefensiveStance.class,
            method = "finisher"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(DefensiveTrainingManual.OOMPH);
        }
    }
}
