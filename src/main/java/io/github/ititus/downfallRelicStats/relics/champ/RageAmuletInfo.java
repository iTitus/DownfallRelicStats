package io.github.ititus.downfallRelicStats.relics.champ;

import champ.relics.RageAmulet;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hermit.util.Wiz;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAoeAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class RageAmuletInfo extends BaseCombatRelicStats {

    private static final RageAmuletInfo INSTANCE = new RageAmuletInfo();

    private RageAmuletInfo() {
        super(RageAmulet.ID);
    }

    public static RageAmuletInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RageAmulet.class,
            method = "addNextTurnPower"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(Wiz.class, "atb", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoeAction.Mode.ONLY_PLAYER, getInstance(), StrengthPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
