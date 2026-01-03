package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.DeadBird;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

public final class DeadBirdInfo extends BaseCombatRelicStats {

    private static final DeadBirdInfo INSTANCE = new DeadBirdInfo();

    private DeadBirdInfo() {
        super(DeadBird.ID);
    }

    public static DeadBirdInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DeadBird.class,
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
