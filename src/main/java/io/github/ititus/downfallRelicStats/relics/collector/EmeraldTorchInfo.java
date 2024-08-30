package io.github.ititus.downfallRelicStats.relics.collector;

import collector.actions.GainReservesAction;
import collector.relics.EmeraldTorch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class EmeraldTorchInfo extends BaseCombatRelicStats {

    private static final EmeraldTorchInfo INSTANCE = new EmeraldTorchInfo();

    private EmeraldTorchInfo() {
        super(EmeraldTorch.ID);
        this.showPerCombat = false;
    }

    public static EmeraldTorchInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = EmeraldTorch.class,
            method = "atBattleStart"
    )
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
