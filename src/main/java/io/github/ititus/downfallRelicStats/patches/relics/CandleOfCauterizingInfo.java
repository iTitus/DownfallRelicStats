package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import theHexaghost.powers.BurnPower;
import theHexaghost.relics.CandleOfCauterizing;

public final class CandleOfCauterizingInfo extends BaseCombatRelicStats {

    private static final CandleOfCauterizingInfo INSTANCE = new CandleOfCauterizingInfo();

    private CandleOfCauterizingInfo() {
        super(CandleOfCauterizing.ID);
    }

    public static CandleOfCauterizingInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CandleOfCauterizing.class,
            method = "onAttack"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(BurnPower.class, Patch.class, 2);
        }

        public static void hook(int amount) {
            getInstance().increaseAmount(amount);
        }
    }
}
