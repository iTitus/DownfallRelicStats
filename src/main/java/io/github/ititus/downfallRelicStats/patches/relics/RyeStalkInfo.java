package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.RyeStalk;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class RyeStalkInfo extends BaseCombatRelicStats {

    private static final RyeStalkInfo INSTANCE = new RyeStalkInfo();

    private RyeStalkInfo() {
        super(RyeStalk.ID);
    }

    public static RyeStalkInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RyeStalk.class,
            method = "wasHPLost"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(RyeStalk.class, "addToTop", Patch.class);
        }

        public static void before() {
            AbstractDungeon.actionManager.addToTop(new CardDrawFollowupAction(getInstance()));
        }

        public static void after() {
            AbstractDungeon.actionManager.addToTop(new PreCardDrawAction(getInstance()));
        }
    }
}
