package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import hermit.relics.CharredGlove;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class CharredGloveInfo extends BaseCombatRelicStats {

    private static final CharredGloveInfo INSTANCE = new CharredGloveInfo();
    private static final int VIGOR_AMOUNT = 4;

    private CharredGloveInfo() {
        super(CharredGlove.ID);
    }

    public static CharredGloveInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CharredGlove.class,
            method = "onCardDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(VIGOR_AMOUNT);
        }
    }
}
