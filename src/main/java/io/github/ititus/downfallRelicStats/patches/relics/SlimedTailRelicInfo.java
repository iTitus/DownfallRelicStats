package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import slimebound.relics.SlimedTailRelic;

public final class SlimedTailRelicInfo extends BaseCombatRelicStats {

    private static final SlimedTailRelicInfo INSTANCE = new SlimedTailRelicInfo();

    private SlimedTailRelicInfo() {
        super(SlimedTailRelic.ID);
        this.showPerTurn = false;
    }

    public static SlimedTailRelicInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SlimedTailRelic.class,
            method = "activate"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix() {
            getInstance().increaseAmount(1);
        }
    }
}
