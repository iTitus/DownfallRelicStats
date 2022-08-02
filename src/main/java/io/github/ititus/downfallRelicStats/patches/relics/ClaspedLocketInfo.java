package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.ClaspedLocket;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class ClaspedLocketInfo extends BaseCombatRelicStats {

    private static final ClaspedLocketInfo INSTANCE = new ClaspedLocketInfo();

    private ClaspedLocketInfo() {
        super(ClaspedLocket.ID);
    }

    public static ClaspedLocketInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ClaspedLocket.class,
            method = "onCardDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, ClaspedLocket.class, "addToTop", Patch.class);
        }

        public static void before(ClaspedLocket __instance) {
            AbstractDungeon.actionManager.addToTop(new CardDrawFollowupAction(getInstance()));
        }

        public static void after(ClaspedLocket __instance) {
            AbstractDungeon.actionManager.addToTop(new PreCardDrawAction(getInstance()));
        }
    }
}
