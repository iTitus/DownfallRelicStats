package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import guardian.relics.PocketSentry;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMultiMethodCallEditor;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.actions.AoePowerFollowupAction;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

public final class PocketSentryInfo extends BaseRelicStats<PocketSentryInfo.Stats> {

    private static final PocketSentryInfo INSTANCE = new PocketSentryInfo();

    private PocketSentryInfo() {
        super(PocketSentry.ID, Stats.class);
    }

    public static PocketSentryInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int damage = 0;
        int weak = 0;
        int artifact = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + damage +
                    description[1] + weak +
                    description[2] + artifact;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(description, 3, damage, totalTurns, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 5, weak, totalTurns, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 7, artifact, totalTurns, totalCombats);
        }
    }

    @SpirePatch(
            clz = PocketSentry.class,
            method = "atTurnStartPostDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preDamageAction;
        private static PreAoePowerAction prePowerActionWeak;
        private static PreAoePowerAction prePowerActionArtifact;

        public static ExprEditor Instrument() {
            return new BeforeAfterMultiMethodCallEditor(PocketSentry.class, "addToBot", Patch.class);
        }

        public static void before(int index, PocketSentry __instance) {
            preDamageAction = null;
            prePowerActionWeak = null;
            prePowerActionArtifact = null;
            if (index == 0) {
                AbstractDungeon.actionManager.addToBottom(preDamageAction = new PreAoeDamageAction());
            } else if (index == 1) {
                AbstractDungeon.actionManager.addToBottom(prePowerActionWeak = new PreAoePowerAction(WeakPower.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(prePowerActionArtifact = new PreAoePowerAction(ArtifactPower.POWER_ID));
            } else {
                throw new AssertionError();
            }
        }

        public static void after(int index, PocketSentry __instance) {
            if (index == 0) {
                AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(by -> getInstance().stats.damage += by, preDamageAction));
            } else if (index == 1) {
                AbstractDungeon.actionManager.addToBottom(new AoePowerFollowupAction(by -> getInstance().stats.weak += by, prePowerActionWeak, true));
                AbstractDungeon.actionManager.addToBottom(new AoePowerFollowupAction(by -> getInstance().stats.artifact += by, prePowerActionArtifact, false));
            } else {
                throw new AssertionError();
            }
        }
    }
}
