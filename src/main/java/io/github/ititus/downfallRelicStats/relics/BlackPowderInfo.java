package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.cards.AbstractHermitCard;
import hermit.relics.BlackPowder;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

public final class BlackPowderInfo extends BaseCombatRelicStats {

    private static final BlackPowderInfo INSTANCE = new BlackPowderInfo();

    private BlackPowderInfo() {
        super(BlackPowder.ID);
    }

    public static BlackPowderInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = AbstractHermitCard.class,
            method = "TriggerDeadOnEffect"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, AbstractHermitCard.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        }
    }
}
