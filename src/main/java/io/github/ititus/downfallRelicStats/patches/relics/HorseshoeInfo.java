package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hermit.relics.Horseshoe;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public final class HorseshoeInfo extends BaseCombatRelicStats {

    private static final HorseshoeInfo INSTANCE = new HorseshoeInfo();

    private HorseshoeInfo() {
        super(Horseshoe.ID);
    }

    public static HorseshoeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Horseshoe.class,
            method = "onReceivePower"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(AbstractPower.class.getName()) && f.getFieldName().equals("amount") && f.isWriter()) {
                        f.replace("{" + Patch1.class.getName() + ".hook(var1.amount,$1);$_=$proceed($$);}");
                    }
                }
            };
        }

        public static void hook(int oldAmount, int newAmount) {
            getInstance().increaseAmount(Math.max(0, oldAmount - newAmount));
        }
    }

    @SpirePatch(
            clz = Horseshoe.class,
            method = "onReceivePowerStacks"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(Horseshoe.class.getName()) && m.getMethodName().equals("flash")) {
                        m.replace("{$_=$proceed($$);" + Patch2.class.getName() + ".hook(stackAmount,stackAmount-1);}");
                    }
                }
            };
        }

        public static void hook(int oldAmount, int newAmount) {
            getInstance().increaseAmount(Math.max(0, oldAmount - newAmount));
        }
    }
}
