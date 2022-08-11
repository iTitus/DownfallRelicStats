package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.ShortStature;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.StatContainer;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class ShortStatureInfo extends BaseRelicStats<ShortStatureInfo.Stats> {

    private static final ShortStatureInfo INSTANCE = new ShortStatureInfo();

    private ShortStatureInfo() {
        super(ShortStature.ID, Stats.class);
    }

    public static ShortStatureInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int healed = 0;
        int timesTriggered = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + timesTriggered +
                    description[1] + healed +
                    description[2] + df.format((double) healed / Math.max(1, timesTriggered));
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(extendedDescription, 1, healed, 0, totalCombats);
        }
    }

    @SpirePatch(
            clz = ShortStature.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static int hp;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "heal", Patch.class);
        }

        public static void before() {
            hp = AbstractDungeon.player.currentHealth;
        }

        public static void after() {
            getInstance().stats.healed += AbstractDungeon.player.currentHealth - hp;
            getInstance().stats.timesTriggered++;
        }
    }
}
