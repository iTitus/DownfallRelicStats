package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import slimebound.actions.TackleSelfDamageAction;
import slimebound.powers.PreventTackleDamagePower;
import slimebound.relics.SelfDamagePreventRelic;

public final class SelfDamagePreventRelicInfo extends BaseCombatRelicStats {

    private static final SelfDamagePreventRelicInfo INSTANCE = new SelfDamagePreventRelicInfo();

    private SelfDamagePreventRelicInfo() {
        super(SelfDamagePreventRelic.ID);
    }

    public static SelfDamagePreventRelicInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = TackleSelfDamageAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(TackleSelfDamageAction __instance, DamageInfo info) {
            if (CardCrawlGame.isInARun() && AbstractDungeon.player.hasRelic(SelfDamagePreventRelic.ID) && !AbstractDungeon.player.hasPower(PreventTackleDamagePower.POWER_ID)) {
                getInstance().increaseAmount(1);
            }
        }
    }
}
