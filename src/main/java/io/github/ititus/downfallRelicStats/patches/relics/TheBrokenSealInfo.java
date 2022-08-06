package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import theHexaghost.relics.TheBrokenSeal;

public final class TheBrokenSealInfo extends BaseCombatRelicStats {

    private static final TheBrokenSealInfo INSTANCE = new TheBrokenSealInfo();

    private TheBrokenSealInfo() {
        super(TheBrokenSeal.ID);
        this.showPerTurn = false;
        this.showPerCombat = false;
    }

    public static TheBrokenSealInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = TheBrokenSeal.class,
            method = "onEquip"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Prefix() {
            getInstance().registerStartingAmount(AbstractDungeon.player.currentHealth);
        }

        public static void Postfix() {
            getInstance().registerEndingAmount(AbstractDungeon.player.currentHealth);
        }
    }
}
