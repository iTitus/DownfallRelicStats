package io.github.ititus.downfallRelicStats.patches.track;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

@SuppressWarnings("unused")
public final class RewardPatches {

    @SpirePatch(
            clz = CombatRewardScreen.class,
            method = "setupItemReward"
    )
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
