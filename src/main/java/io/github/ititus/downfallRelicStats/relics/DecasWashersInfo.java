package io.github.ititus.downfallRelicStats.relics;

import automaton.relics.DecasWashers;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class DecasWashersInfo extends BaseCombatRelicStats {

    private static final DecasWashersInfo INSTANCE = new DecasWashersInfo();

    private DecasWashersInfo() {
        super(DecasWashers.ID);
    }

    public static DecasWashersInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = DecasWashers.class,
            method = "atTurnStartPostDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, DecasWashers.class, "addToBot", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
