package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import guardian.relics.ObsidianScales;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAoeAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class ObsidianScalesInfo extends BaseCombatRelicStats {

    private static final ObsidianScalesInfo INSTANCE = new ObsidianScalesInfo();

    private ObsidianScalesInfo() {
        super(ObsidianScales.ID);
    }

    public static ObsidianScalesInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ObsidianScales.class,
            method = "atBattleStart"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ObsidianScales.class, "addToTop", Patch1.class);
        }

        public static void before() {
            preAction = new PreAoePowerAction(PreAoeAction.Mode.ONLY_PLAYER, getInstance(), ThornsPower.POWER_ID);
            AbstractDungeon.actionManager.addToTop(preAction.post());
        }

        public static void after() {
            AbstractDungeon.actionManager.addToTop(preAction);
        }
    }

    @SpirePatch(
            clz = ObsidianScales.class,
            method = "onReceivePower"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ObsidianScales.class, "addToBot", Patch2.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoePowerAction(PreAoeAction.Mode.ONLY_PLAYER, getInstance(), ThornsPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(preAction.post());
        }
    }
}
