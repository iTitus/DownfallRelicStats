package io.github.ititus.downfallRelicStats.relics.slimebound;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
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
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(new PreHealingAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new HealingFollowupAction(getInstance()));
        }
    }
}
