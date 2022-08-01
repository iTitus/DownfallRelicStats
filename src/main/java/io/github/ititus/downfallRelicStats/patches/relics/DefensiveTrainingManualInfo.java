package io.github.ititus.downfallRelicStats.patches.relics;

import champ.relics.DefensiveTrainingManual;
import champ.stances.DefensiveStance;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public final class DefensiveTrainingManualInfo extends BaseCombatRelicStats {

    private static final DefensiveTrainingManualInfo INSTANCE = new DefensiveTrainingManualInfo();
    private static final int DEFENSIVE_FINISHER_DEFAULT_BLOCK_AMOUNT = 10;

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
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(DefensiveStance.class.getName()) && m.getMethodName().equals("finisherAmount")) {
                        m.replace("{$_ = $proceed($$); " + Patch.class.getName() + ".patch($_);}");
                    }
                }
            };
        }

        public static void patch(int finisherAmount) {
            getInstance().increaseAmount(finisherAmount - DEFENSIVE_FINISHER_DEFAULT_BLOCK_AMOUNT);
        }
    }
}
