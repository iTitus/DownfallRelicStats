package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import hermit.relics.CharredGlove;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.actions.AoePowerFollowupAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import javassist.expr.ExprEditor;

public final class CharredGloveInfo extends BaseCombatRelicStats {

    private static final CharredGloveInfo INSTANCE = new CharredGloveInfo();

    private CharredGloveInfo() {
        super(CharredGlove.ID);
    }

    public static CharredGloveInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CharredGlove.class,
            method = "onCardDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(VigorPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoePowerFollowupAction(getInstance(), preAction));
        }
    }
}
