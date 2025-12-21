package io.github.ititus.downfallRelicStats.relics.snecko;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import sneckomod.relics.LoadedDie;

public final class LoadedDieInfo extends BaseCombatRelicStats {

    private static final LoadedDieInfo INSTANCE = new LoadedDieInfo();

    private LoadedDieInfo() {
        super(LoadedDie.ID);
    }

    public static LoadedDieInfo getInstance() {
        return INSTANCE;
    }


    @SpirePatch(
            clz = LoadedDie.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(blockAmount);
        }
    }
}
