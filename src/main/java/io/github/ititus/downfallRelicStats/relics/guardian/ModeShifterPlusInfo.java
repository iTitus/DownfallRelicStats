package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import guardian.relics.ModeShifterPlus;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class ModeShifterPlusInfo extends BaseCombatRelicStats {

    private static final ModeShifterPlusInfo INSTANCE = new ModeShifterPlusInfo();

    private ModeShifterPlusInfo() {
        super(ModeShifterPlus.ID);
    }

    public static ModeShifterPlusInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ModeShifterPlus.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix() {
            getInstance().trigger();
        }
    }
}
