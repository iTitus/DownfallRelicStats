package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.WoundPoker;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

public final class WoundPokerInfo extends BaseCombatRelicStats {

    private static final WoundPokerInfo INSTANCE = new WoundPokerInfo();

    private WoundPokerInfo() {
        super(WoundPoker.ID);
    }

    public static WoundPokerInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = WoundPoker.class,
            method = "onPlayerEndTurn"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        }
    }
}
