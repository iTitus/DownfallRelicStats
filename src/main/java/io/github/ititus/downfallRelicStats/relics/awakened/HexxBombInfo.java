package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.powers.ManaburnPower;
import awakenedOne.relics.HexxBomb;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PostAoePowerAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class HexxBombInfo extends BaseCombatRelicStats {

    private static final HexxBombInfo INSTANCE = new HexxBombInfo();

    private HexxBombInfo() {
        super(HexxBomb.ID);
    }

    public static HexxBombInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = HexxBomb.class,
            method = "onMonsterDeath"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, HexxBomb.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoePowerAction.Mode.ONLY_MONSTERS, ManaburnPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new PostAoePowerAction(getInstance(), preAction));
        }
    }
}
