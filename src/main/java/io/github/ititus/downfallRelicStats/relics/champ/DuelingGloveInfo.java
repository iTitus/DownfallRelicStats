package io.github.ititus.downfallRelicStats.relics.champ;

import champ.relics.DuelingGlove;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class DuelingGloveInfo extends BaseCombatRelicStats {

    private static final DuelingGloveInfo INSTANCE = new DuelingGloveInfo();

    private DuelingGloveInfo() {
        super(DuelingGlove.ID);
    }

    public static DuelingGloveInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DuelingGlove.class,
            method = "onPlayCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(getInstance(), VulnerablePower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
