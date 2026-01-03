package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.ShreddedDoll;
import awakenedOne.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RitualPower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class ShreddedDollInfo extends BaseCombatRelicStats {

    private static final ShreddedDollInfo INSTANCE = new ShreddedDollInfo();

    private ShreddedDollInfo() {
        super(ShreddedDoll.ID);
        this.showPerTurn = false;
    }

    public static ShreddedDollInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ShreddedDoll.class,
            method = "onAwaken"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(Wiz.class, "applyToSelf", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_PLAYER, RitualPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction(getInstance(), preAction));
        }
    }
}
