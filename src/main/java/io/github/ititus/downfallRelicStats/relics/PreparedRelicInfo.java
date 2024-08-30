package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import slimebound.relics.PreparedRelic;

public final class PreparedRelicInfo extends BaseCombatRelicStats {

    private static final PreparedRelicInfo INSTANCE = new PreparedRelicInfo();

    private PreparedRelicInfo() {
        super(PreparedRelic.ID);
        this.showPerTurn = false;
    }

    public static PreparedRelicInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = PreparedRelic.class,
            method = "atTurnStart"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, GameActionManager.class, "addToBottom", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
