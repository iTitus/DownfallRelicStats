package io.github.ititus.downfallRelicStats.patches.track;

import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

@SuppressWarnings("unused")
public final class RewardPatches {

    /*TODO: @SpirePatch(
            clz = CombatRewardScreen.class,
            method = "setupItemReward"
    )*/
    public static final class GemRewardPath {

        public static void Postfix(CombatRewardScreen __instance) {
            if (__instance.rewards != null) {
                for (RewardItem reward : __instance.rewards) {
                    SourceModifier.applyTo(reward);
                }
            }
        }
    }
}
