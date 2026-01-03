package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.powers.ManaburnPower;
import awakenedOne.relics.TomeOfPortalmancy;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;

public final class TomeOfPortalmancyInfo extends BaseCombatRelicStats {

    private static final TomeOfPortalmancyInfo INSTANCE = new TomeOfPortalmancyInfo();

    private TomeOfPortalmancyInfo() {
        super(TomeOfPortalmancy.ID);
    }

    public static TomeOfPortalmancyInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = TomeOfPortalmancy.class,
            method = "onSpecificTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static void Prefix() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(ManaburnPower.POWER_ID));
        }

        public static void Postfix() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction(getInstance(), preAction));
        }
    }
}
