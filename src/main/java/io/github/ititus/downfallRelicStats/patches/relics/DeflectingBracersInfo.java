package io.github.ititus.downfallRelicStats.patches.relics;

import champ.powers.CounterPower;
import champ.relics.DeflectingBracers;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class DeflectingBracersInfo extends BaseCombatRelicStats {

    private static final DeflectingBracersInfo INSTANCE = new DeflectingBracersInfo();

    private DeflectingBracersInfo() {
        super(DeflectingBracers.ID);
    }

    public static DeflectingBracersInfo getInstance() {
        return INSTANCE;
    }

    // TODO: fix this (crashes because of ClassLoader)
    /*@SpirePatch(
            clz = ChampMod.class,
            method = "receiveOnPlayerLoseBlock"
    )*/
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(CounterPower.class, Patch.class, 1);
        }

        public static void hook(int counterAmount) {
            getInstance().increaseAmount(counterAmount);
        }
    }
}
