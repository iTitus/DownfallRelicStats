package io.github.ititus.downfallRelicStats.patches.editor;

import javassist.CannotCompileException;
import javassist.expr.MethodCall;

public class BeforeAfterMethodCallEditor extends SafeExprEditor {

    private final int requiredIndex;
    private final String requiredTargetClassName;
    private final String requiredTargetMethodName;
    private final String callbackClassName;
    private final boolean doBefore;
    private final boolean doAfter;
    private final boolean addThis;

    private int n;

    public BeforeAfterMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName());
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass) {
        this(requiredIndex, requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName());
    }

    public BeforeAfterMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass, boolean doBefore, boolean doAfter) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName(), doBefore, doAfter);
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass, boolean doBefore, boolean doAfter) {
        this(requiredIndex, requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName(), doBefore, doAfter);
    }

    public BeforeAfterMethodCallEditor(Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass, boolean doBefore, boolean doAfter, boolean addThis) {
        this(requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName(), doBefore, doAfter, addThis);
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, Class<?> requiredTargetClass, String requiredTargetMethodName, Class<?> callbackClass, boolean doBefore, boolean doAfter, boolean addThis) {
        this(requiredIndex, requiredTargetClass.getName(), requiredTargetMethodName, callbackClass.getName(), doBefore, doAfter, addThis);
    }

    public BeforeAfterMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName) {
        this(0, requiredTargetClassName, requiredTargetMethodName, callbackClassName);
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName) {
        this(requiredIndex, requiredTargetClassName, requiredTargetMethodName, callbackClassName, true, true);
    }

    public BeforeAfterMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName, boolean doBefore, boolean doAfter) {
        this(0, requiredTargetClassName, requiredTargetMethodName, callbackClassName, doBefore, doAfter);
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName, boolean doBefore, boolean doAfter) {
        this(requiredIndex, requiredTargetClassName, requiredTargetMethodName, callbackClassName, doBefore, doAfter, false);
    }

    public BeforeAfterMethodCallEditor(String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName, boolean doBefore, boolean doAfter, boolean addThis) {
        this(0, requiredTargetClassName, requiredTargetMethodName, callbackClassName, doBefore, doAfter, addThis);
    }

    public BeforeAfterMethodCallEditor(int requiredIndex, String requiredTargetClassName, String requiredTargetMethodName, String callbackClassName, boolean doBefore, boolean doAfter, boolean addThis) {
        this.requiredIndex = requiredIndex;
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
            if (n++ != requiredIndex) {
                return;
            }

            StringBuilder b = new StringBuilder().append('{');
            if (doBefore) {
                b.append(callbackClassName).append(".before(");
                if (addThis) {
                    b.append("this");
                }
                b.append(");");
            }
            b.append("$_=$proceed($$);");
            if (doAfter) {
                b.append(callbackClassName).append(".after(");
                if (addThis) {
                    b.append("this");
                }
                b.append(");");
            }
            m.replace(b.append('}').toString());
        }
    }
}
