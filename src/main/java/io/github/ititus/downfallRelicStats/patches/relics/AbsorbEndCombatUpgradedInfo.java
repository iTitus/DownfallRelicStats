package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.HealingFollowupAction;
import relicstats.actions.PreHealingAction;
import slimebound.relics.AbsorbEndCombatUpgraded;

public final class AbsorbEndCombatUpgradedInfo extends BaseCombatRelicStats {

    private static final AbsorbEndCombatUpgradedInfo INSTANCE = new AbsorbEndCombatUpgradedInfo();

    private AbsorbEndCombatUpgradedInfo() {
        super(AbsorbEndCombatUpgraded.ID);
    }

    public static AbsorbEndCombatUpgradedInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = AbsorbEndCombatUpgraded.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before(AbsorbEndCombatUpgraded __instance) {
            AbstractDungeon.actionManager.addToBottom(new PreHealingAction(getInstance()));
        }

        public static void after(AbsorbEndCombatUpgraded __instance) {
            AbstractDungeon.actionManager.addToBottom(new HealingFollowupAction(getInstance()));
        }
    }
}
