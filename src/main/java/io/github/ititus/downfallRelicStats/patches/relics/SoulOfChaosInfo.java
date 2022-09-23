package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import theHexaghost.ghostflames.MayhemGhostflame;
import theHexaghost.relics.SoulOfChaos;

public final class SoulOfChaosInfo extends BaseCombatRelicStats {

    private static final SoulOfChaosInfo INSTANCE = new SoulOfChaosInfo();

    private SoulOfChaosInfo() {
        super(SoulOfChaos.ID);
    }

    public static SoulOfChaosInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MayhemGhostflame.class,
            method = "onCharge"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix() {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() > 0) {
                getInstance().increaseAmount(1);
            }
        }
    }
}
