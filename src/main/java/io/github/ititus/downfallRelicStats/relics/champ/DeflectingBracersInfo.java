package io.github.ititus.downfallRelicStats.relics.champ;

import champ.ChampMod;
import champ.powers.CounterPower;
import champ.relics.DeflectingBracers;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class DeflectingBracersInfo extends BaseCombatRelicStats {

    private static final DeflectingBracersInfo INSTANCE = new DeflectingBracersInfo();

    private DeflectingBracersInfo() {
        super(DeflectingBracers.ID);
    }

    public static DeflectingBracersInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ChampMod.class,
            method = "receiveOnPlayerLoseBlock"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_PLAYER, CounterPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction(getInstance(), preAction));
        }
    }
}
