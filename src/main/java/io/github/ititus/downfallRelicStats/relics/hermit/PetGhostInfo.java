package io.github.ititus.downfallRelicStats.relics.hermit;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import hermit.relics.PetGhost;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class PetGhostInfo extends BaseCombatRelicStats {

    private static final PetGhostInfo INSTANCE = new PetGhostInfo();

    private PetGhostInfo() {
        super(PetGhost.ID);
        this.showPerTurn = false;
    }

    public static PetGhostInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = PetGhost.class,
            method = "onPlayerDeath"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(PetGhost.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
