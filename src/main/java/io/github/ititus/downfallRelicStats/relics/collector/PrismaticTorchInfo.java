package io.github.ititus.downfallRelicStats.relics.collector;

import collector.actions.GainReservesAction;
import collector.relics.PrismaticTorch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
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

    @SpirePatches({
            @SpirePatch(
                    clz = PrismaticTorch.class,
                    method = "atBattleStart"
            ),
            @SpirePatch(
                    clz = PrismaticTorch.class,
                    method = "onExhaust"
            )
    })
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainReservesAction.class, Patch.class, 1);
        }

        public static void hook(int reserveAmount) {
            getInstance().increaseAmount(reserveAmount);
        }
    }
}
