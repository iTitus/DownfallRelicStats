package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.DeadBird;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAoeDamageAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

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
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
