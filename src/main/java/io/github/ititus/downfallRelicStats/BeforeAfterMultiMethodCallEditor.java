package io.github.ititus.downfallRelicStats;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BeforeAfterMultiMethodCallEditor extends ExprEditor {

    private final String requiredTargetClassName;
    private final String requiredTargetMethodName;
    private final String callbackClassName;

    private int n;

    public BeforeAfterMultiMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName());
    }

    public BeforeAfterMultiMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName) {
        this.requiredTargetClassName = requiredTargetClassName;
        this.requiredTargetMethodName = requiredTargetMethodName;
        this.callbackClassName = callbackClassName;
    }

    @Override
    public void edit(MethodCall m) throws CannotCompileException {
        if (m.getClassName().equals(requiredTargetClassName) && m.getMethodName().equals(requiredTargetMethodName)) {
            m.replace("{" + callbackClassName + ".before(" + n + ", this); $proceed($$); " + callbackClassName + ".after(" + n + ", this);}");
            n++;
        }
    }
}
