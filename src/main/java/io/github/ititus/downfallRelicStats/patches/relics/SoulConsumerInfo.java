package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
            method = "onVictory"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static int health;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "heal", Patch.class);
        }

        public static void before() {
            getInstance().registerStartingAmount(AbstractDungeon.player.currentHealth);
        }

        public static void after() {
            getInstance().registerEndingAmount(AbstractDungeon.player.currentHealth);
        }
    }
}
