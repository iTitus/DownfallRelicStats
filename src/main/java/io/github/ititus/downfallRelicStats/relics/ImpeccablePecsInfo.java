package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import gremlin.relics.ImpeccablePecs;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class ImpeccablePecsInfo extends BaseCombatRelicStats {

    private static final ImpeccablePecsInfo INSTANCE = new ImpeccablePecsInfo();

    private ImpeccablePecsInfo() {
        super(ImpeccablePecs.ID);
    }

    public static ImpeccablePecsInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ImpeccablePecs.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(AddTemporaryHPAction.class, Patch.class, 3);
        }

        public static void hook(int amount) {
            getInstance().increaseAmount(amount);
        }
    }
}
