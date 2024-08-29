package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import guardian.relics.PocketSentry;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.AoePowerFollowupAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.PreAoeDamageAction;

public final class PocketSentryInfo extends BaseCombatRelicStats {

    private static final PocketSentryInfo INSTANCE = new PocketSentryInfo();

    private PocketSentryInfo() {
        super(PocketSentry.ID);
    }

    public static PocketSentryInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = PocketSentry.class,
            method = "atTurnStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preDamageAction;
        private static PreAoePowerAction prePowerAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(PocketSentry.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(prePowerAction = new PreAoePowerAction(WeakPower.POWER_ID));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoePowerFollowupAction(getInstance(), prePowerAction));
        }
    }
}
