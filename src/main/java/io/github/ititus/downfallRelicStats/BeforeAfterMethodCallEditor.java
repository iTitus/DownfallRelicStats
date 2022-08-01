package io.github.ititus.downfallRelicStats;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BeforeAfterMethodCallEditor extends ExprEditor {

    private final int requiredIndex;
    private final String requiredTargetClassName;
    private final String requiredTargetMethodName;
    private final String callbackClassName;

    private int n;

    public BeforeAfterMethodCallEditor(int requiredIndex, Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass) {
        this(requiredIndex, requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName());
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName) {
        this.requiredIndex = requiredIndex;
        this.requiredTargetClassName = requiredTargetClassName;
        this.requiredTargetMethodName = requiredTargetMethodName;
        this.callbackClassName = callbackClassName;
    }

    @Override
    public void edit(MethodCall m) throws CannotCompileException {
        if (m.getClassName().equals(requiredTargetClassName) && m.getMethodName().equals(requiredTargetMethodName)) {
            if (n++ != requiredIndex) {
                return;
            }

            m.replace("{" + callbackClassName + ".before(this); $proceed($$); " + callbackClassName + ".after(this);}");
        }
    }
}
