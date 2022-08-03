package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.HealingFollowupAction;
import relicstats.actions.PreHealingAction;
import slimebound.relics.AbsorbEndCombat;

public final class AbsorbEndCombatInfo extends BaseCombatRelicStats {

    private static final AbsorbEndCombatInfo INSTANCE = new AbsorbEndCombatInfo();

    private AbsorbEndCombatInfo() {
        super(AbsorbEndCombat.ID);
    }

    public static AbsorbEndCombatInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = AbsorbEndCombat.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before(AbsorbEndCombat __instance) {
            AbstractDungeon.actionManager.addToBottom(new PreHealingAction(getInstance()));
        }

        public static void after(AbsorbEndCombat __instance) {
            AbstractDungeon.actionManager.addToBottom(new HealingFollowupAction(getInstance()));
        }
    }
}
