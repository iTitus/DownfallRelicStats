package io.github.ititus.downfallRelicStats.relics.collector;

import collector.powers.DoomPower;
import collector.relics.JadeRing;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class JadeRingInfo extends BaseCombatRelicStats {

    private static final JadeRingInfo INSTANCE = new JadeRingInfo();

    private JadeRingInfo() {
        super(JadeRing.ID);
    }

    public static JadeRingInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DoomPower.class,
            method = "explode"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(DoomPower __instance) {
            if (AbstractDungeon.player.hasRelic(JadeRing.ID)) {
                // TODO: track exact amount, but that might be hard to do
                getInstance().trigger();
            }
        }
    }
}
