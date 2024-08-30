package io.github.ititus.downfallRelicStats.relics.collector;

import collector.actions.GainReservesAction;
import collector.relics.PrismaticTorch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class PrismaticTorchInfo extends BaseCombatRelicStats {

    private static final PrismaticTorchInfo INSTANCE = new PrismaticTorchInfo();

    private PrismaticTorchInfo() {
        super(PrismaticTorch.ID);
    }

    public static PrismaticTorchInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = PrismaticTorch.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainReservesAction.class, Patch1.class, 1);
        }

        public static void hook(int reserveAmount) {
            // TODO: also track embers created?
            getInstance().increaseAmount(reserveAmount);
        }
    }

    @SpirePatch(
            clz = PrismaticTorch.class,
            method = "onExhaust"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainReservesAction.class, Patch2.class, 1);
        }

        public static void hook(int reserveAmount) {
            getInstance().increaseAmount(reserveAmount);
        }
    }
}
