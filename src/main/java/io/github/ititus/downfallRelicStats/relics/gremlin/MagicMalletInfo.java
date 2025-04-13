package io.github.ititus.downfallRelicStats.relics.gremlin;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.powers.WizPower;
import gremlin.relics.MagicalMallet;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class MagicMalletInfo extends BaseCombatRelicStats {

    private static final MagicMalletInfo INSTANCE = new MagicMalletInfo();

    private MagicMalletInfo() {
        super(MagicalMallet.ID);
    }

    public static MagicMalletInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MagicalMallet.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(MagicalMallet.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_PLAYER, WizPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction(getInstance(), preAction));
        }
    }
}
