package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import gremlin.relics.WizardHat;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.actions.AoePowerFollowupAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import javassist.expr.ExprEditor;

public final class WizardHatInfo extends BaseCombatRelicStats {

    private static final WizardHatInfo INSTANCE = new WizardHatInfo();

    private WizardHatInfo() {
        super(WizardHat.ID);
    }

    public static WizardHatInfo getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPowerChanged(String powerId, int amount) {
        increaseAmount(1);
    }

    @SpirePatch(
            clz = WizardHat.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(WizardHat.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_PLAYER, p -> p.type == AbstractPower.PowerType.DEBUFF));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoePowerFollowupAction(getInstance(), preAction));
        }
    }
}
