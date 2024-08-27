package io.github.ititus.downfallRelicStats.patches.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
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
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
