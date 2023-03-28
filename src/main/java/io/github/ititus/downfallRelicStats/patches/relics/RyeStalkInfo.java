package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hermit.relics.RyeStalk;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.actions.AoePowerFollowupAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import javassist.expr.ExprEditor;

public final class RyeStalkInfo extends BaseCombatRelicStats {

    private static final RyeStalkInfo INSTANCE = new RyeStalkInfo();

    private RyeStalkInfo() {
        super(RyeStalk.ID);
        this.powerChangeInvert = true;
    }

    public static RyeStalkInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RyeStalk.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, RyeStalk.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(StrengthPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoePowerFollowupAction(getInstance(), preAction));
        }
    }
}
