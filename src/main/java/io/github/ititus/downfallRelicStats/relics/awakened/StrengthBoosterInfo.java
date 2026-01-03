package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.StrengthBooster;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class StrengthBoosterInfo extends BaseCombatRelicStats {

    private static final StrengthBoosterInfo INSTANCE = new StrengthBoosterInfo();

    private StrengthBoosterInfo() {
        super(StrengthBooster.ID);
    }

    public static StrengthBoosterInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = StrengthBooster.class,
            method = "onSpecificTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(StrengthBooster __instance, int amount) {
            getInstance().increaseAmount(amount);
        }
    }
}
