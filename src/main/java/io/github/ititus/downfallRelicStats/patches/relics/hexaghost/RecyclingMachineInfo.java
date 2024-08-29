package io.github.ititus.downfallRelicStats.patches.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.relics.RecyclingMachine;

public final class RecyclingMachineInfo extends BaseCombatRelicStats {

    private static final RecyclingMachineInfo INSTANCE = new RecyclingMachineInfo();

    private RecyclingMachineInfo() {
        super(RecyclingMachine.ID);
        this.showPerTurn = false;
    }

    public static RecyclingMachineInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RecyclingMachine.class,
            method = "onExhaust"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(GameActionManager.class, "addToBottom", Patch.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
