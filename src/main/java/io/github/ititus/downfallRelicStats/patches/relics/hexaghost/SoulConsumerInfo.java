package io.github.ititus.downfallRelicStats.patches.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.SoulConsumer;

public final class SoulConsumerInfo extends BaseCombatRelicStats {

    private static final SoulConsumerInfo INSTANCE = new SoulConsumerInfo();

    private SoulConsumerInfo() {
        super(SoulConsumer.ID);
        this.showPerTurn = false;
    }

    public static SoulConsumerInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SoulConsumer.class,
            method = "onExhaust"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(SoulConsumer.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
