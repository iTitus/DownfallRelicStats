package io.github.ititus.downfallRelicStats.relics.shared;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import downfall.relics.HeartBlessingGreen;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class HeartBlessingGreenInfo extends BaseCombatRelicStats {

    private static final HeartBlessingGreenInfo INSTANCE = new HeartBlessingGreenInfo();

    private HeartBlessingGreenInfo() {
        super(HeartBlessingGreen.ID);
        this.showPerTurn = false;
    }

    public static HeartBlessingGreenInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = HeartBlessingGreen.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(HeartBlessingGreen.class, "addToTop", Patch.class);
        }

        public static void before() {
            preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_PLAYER, DexterityPower.POWER_ID);
            AbstractDungeon.actionManager.addToTop(new PostAoePowerAction(getInstance(), preAction));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToTop(preAction);
        }
    }
}
