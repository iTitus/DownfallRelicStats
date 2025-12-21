package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import guardian.relics.PickAxe;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.util.ArrayList;

public final class PickAxeInfo extends BaseCombatRelicStats {

    private static final PickAxeInfo INSTANCE = new PickAxeInfo();

    private PickAxeInfo() {
        super(PickAxe.ID);
        this.showPerTurn = false;
    }

    public static PickAxeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatches({
            @SpirePatch(
                    clz = PickAxe.class,
                    method = "onEquip"
            ),
            @SpirePatch(
                    clz = PickAxe.class,
                    method = "onVictory"
            )
    })
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(ArrayList.class, "add", Patch1.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
