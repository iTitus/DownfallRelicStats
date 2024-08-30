package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.SoullitLamp;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class SoullitLampInfo extends BaseCombatRelicStats {

    private static final SoullitLampInfo INSTANCE = new SoullitLampInfo();

    private SoullitLampInfo() {
        super(SoullitLamp.ID);
        this.showPerCombat = false;
    }

    public static SoullitLampInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SoullitLamp.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix() {
            getInstance().increaseAmount(1);
        }
    }
}
