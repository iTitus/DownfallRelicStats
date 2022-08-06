package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.actions.AoePowerFollowupAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import javassist.expr.ExprEditor;
import theHexaghost.powers.BurnPower;
import theHexaghost.relics.CandleOfCauterizing;

public final class CandleOfCauterizingInfo extends BaseCombatRelicStats {

    private static final CandleOfCauterizingInfo INSTANCE = new CandleOfCauterizingInfo();

    private CandleOfCauterizingInfo() {
        super(CandleOfCauterizing.ID);
    }

    public static CandleOfCauterizingInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CandleOfCauterizing.class,
            method = "onAttack"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToTop", Patch.class);
        }

        public static void before() {
            preAction = new PreAoePowerAction(BurnPower.POWER_ID);
            AbstractDungeon.actionManager.addToTop(new AoePowerFollowupAction(getInstance(), preAction));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToTop(preAction);
        }
    }
}
