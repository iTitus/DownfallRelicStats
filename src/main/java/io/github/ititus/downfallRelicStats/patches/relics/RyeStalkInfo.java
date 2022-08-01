package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.powers.Bruise;
import hermit.relics.RyeStalk;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

public final class RyeStalkInfo extends BaseCombatRelicStats {

    private static final RyeStalkInfo INSTANCE = new RyeStalkInfo();

    private RyeStalkInfo() {
        super(RyeStalk.ID);
    }

    public static RyeStalkInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Bruise.class,
            method = "atStartOfTurn"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, Bruise.class, "addToBot", Patch.class);
        }

        public static void before(Bruise __instance) {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        }

        public static void after(Bruise __instance) {
            AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        }
    }
}
