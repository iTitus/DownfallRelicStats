package io.github.ititus.downfallRelicStats.patches.relics;

import collector.patches.DrawFromCollection;
import collector.relics.BlockedChakra;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class BlockedChakraInfo extends BaseCombatRelicStats {

    private static final BlockedChakraInfo INSTANCE = new BlockedChakraInfo();

    private BlockedChakraInfo() {
        super(BlockedChakra.ID);
        this.showPerTurn = false;
    }

    public static BlockedChakraInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DrawFromCollection.AbstractPlayerApplyStartOfTurnPostDrawRelicsPatch.class,
            method = "Prefix"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
