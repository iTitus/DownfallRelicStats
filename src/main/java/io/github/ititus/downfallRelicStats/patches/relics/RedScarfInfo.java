package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import hermit.relics.RedScarf;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class RedScarfInfo extends BaseCombatRelicStats {

    private static final RedScarfInfo INSTANCE = new RedScarfInfo();

    private RedScarfInfo() {
        super(RedScarf.ID);
    }

    public static RedScarfInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RedScarf.class,
            method = "onApplyPower"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch.class, 3);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(blockAmount);
        }
    }
}
