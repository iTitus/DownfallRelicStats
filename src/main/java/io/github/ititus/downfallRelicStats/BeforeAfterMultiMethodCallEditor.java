package io.github.ititus.downfallRelicStats;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BeforeAfterMultiMethodCallEditor extends ExprEditor {

    private final String requiredTargetClassName;
    private final String requiredTargetMethodName;
    private final String callbackClassName;
    private final boolean doBefore;
    private final boolean doAfter;
    private final boolean addThis;

    private int n;

    public BeforeAfterMultiMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName());
    }

    public BeforeAfterMultiMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass, boolean doBefore, boolean doAfter) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName(), doBefore, doAfter);
    }

    public BeforeAfterMultiMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass, boolean doBefore, boolean doAfter, boolean addThis) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName(), doBefore, doAfter, addThis);
    }

    public BeforeAfterMultiMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName) {
        this(requiredTargetClassName, requiredTargetMethodName, callbackClassName, true, true, true);
    }

    public BeforeAfterMultiMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName, boolean doBefore, boolean doAfter) {
        this(requiredTargetClassName, requiredTargetMethodName, callbackClassName, doBefore, doAfter, false);
    }

    public BeforeAfterMultiMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName, boolean doBefore, boolean doAfter, boolean addThis) {
        this.requiredTargetClassName = requiredTargetClassName;
        this.requiredTargetMethodName = requiredTargetMethodName;
        this.callbackClassName = callbackClassName;
        this.doBefore = doBefore;
        this.doAfter = doAfter;
        this.addThis = addThis;
    }

    @Override
    public void edit(MethodCall m) throws CannotCompileException {
        if (m.getClassName().equals(requiredTargetClassName) && m.getMethodName().equals(requiredTargetMethodName)) {
            StringBuilder b = new StringBuilder().append('{');
            if (doBefore) {
                b.append(callbackClassName).append(".before(").append(n);
                if (addThis) {
                    b.append(",this");
                }
                b.append(");");
            }
            b.append("$_=$proceed($$);");
            if (doAfter) {
                b.append(callbackClassName).append(".after(").append(n);
                if (addThis) {
                    b.append(",this");
                }
                b.append(");");
            }
            m.replace(b.append('}').toString());

            n++;
        }
    }
}
