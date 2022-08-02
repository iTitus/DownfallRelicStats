package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import guardian.actions.BraceAction;
import guardian.relics.DefensiveModeMoreBlock;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class DefensiveModeMoreBlockInfo extends BaseCombatRelicStats {

    private static final DefensiveModeMoreBlockInfo INSTANCE = new DefensiveModeMoreBlockInfo();

    private static final int ADDITIONAL_BRACE = 1;

    private DefensiveModeMoreBlockInfo() {
        super(DefensiveModeMoreBlock.ID);
    }

    public static DefensiveModeMoreBlockInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BraceAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                int n = 0;

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("braceValue") && n++ == 2) {
                        f.replace("{$_ = $proceed($$);" + Patch.class.getName() + ".hook();}");
                    }
                }
            };
        }

        public static void hook() {
            getInstance().increaseAmount(ADDITIONAL_BRACE);
        }
    }
}
