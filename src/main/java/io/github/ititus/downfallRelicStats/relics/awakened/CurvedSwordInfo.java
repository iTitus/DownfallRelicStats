package io.github.ititus.downfallRelicStats.relics.awakened;

import awakenedOne.relics.CurvedSword;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.actions.PreAoePowerAction;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class CurvedSwordInfo extends BaseCombatRelicStats {

    private static final CurvedSwordInfo INSTANCE = new CurvedSwordInfo();

    private CurvedSwordInfo() {
        super(CurvedSword.ID);
        this.showPerTurn = false;
    }

    public static CurvedSwordInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = CurvedSword.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoePowerAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(CurvedSword.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
