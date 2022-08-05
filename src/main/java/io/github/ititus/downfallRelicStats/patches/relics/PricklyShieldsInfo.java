package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.PricklyShields;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

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
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        }
    }
}
