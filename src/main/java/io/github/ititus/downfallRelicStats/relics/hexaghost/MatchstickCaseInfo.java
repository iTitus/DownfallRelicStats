package io.github.ititus.downfallRelicStats.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import theHexaghost.ghostflames.AbstractGhostflame;
import theHexaghost.relics.MatchstickCase;

public final class MatchstickCaseInfo extends BaseCombatRelicStats {

    private static final MatchstickCaseInfo INSTANCE = new MatchstickCaseInfo();

    private MatchstickCaseInfo() {
        super(MatchstickCase.ID);
    }

    public static MatchstickCaseInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MatchstickCase.class,
            method = "onCharge"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Prefix(MatchstickCase __instance, AbstractGhostflame chargedFlame, boolean ___triggered) {
            if (!___triggered) {
                // TODO: try to differentiate what the player has picked
                // difficulty: uses exact same logic as Float+
                getInstance().trigger();
            }
        }
    }
}
