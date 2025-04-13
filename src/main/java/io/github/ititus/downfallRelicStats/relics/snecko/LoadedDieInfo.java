package io.github.ititus.downfallRelicStats.relics.snecko;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import sneckomod.actions.MuddleAction;
import sneckomod.actions.MuddleMarkedAction;
import sneckomod.relics.LoadedDie;

public final class LoadedDieInfo extends BaseCombatRelicStats {

    private static final LoadedDieInfo INSTANCE = new LoadedDieInfo();

    private LoadedDieInfo() {
        super(LoadedDie.ID);
    }

    public static LoadedDieInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatches({
            @SpirePatch(
                    clz = MuddleAction.class,
                    method = "update"
            ),
            @SpirePatch(
                    clz = MuddleMarkedAction.class,
                    method = "update"
            )
    })
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
