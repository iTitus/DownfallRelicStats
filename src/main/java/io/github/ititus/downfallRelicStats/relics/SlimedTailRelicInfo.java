package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;
import slimebound.relics.SlimedTailRelic;

public final class SlimedTailRelicInfo extends BaseCombatRelicStats {

    private static final SlimedTailRelicInfo INSTANCE = new SlimedTailRelicInfo();

    private SlimedTailRelicInfo() {
        super(SlimedTailRelic.ID);
    }

    public static SlimedTailRelicInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SlimedTailRelic.class,
            method = "onChannel"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, SlimedTailRelicInfo.Patch.class, 2);
        }

        public static void hook(int blockAmount) {
            getInstance().increaseAmount(blockAmount);
        }
    }
}
