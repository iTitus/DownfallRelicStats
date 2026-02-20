package io.github.ititus.downfallRelicStats.relics.gremlin;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.PricklyShields;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAoeDamageAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class PricklyShieldsInfo extends BaseCombatRelicStats {

    private static final PricklyShieldsInfo INSTANCE = new PricklyShieldsInfo();

    private PricklyShieldsInfo() {
        super(PricklyShields.ID);
    }

    public static PricklyShieldsInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = PricklyShields.class,
            method = "onPlayerGainedBlock"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
