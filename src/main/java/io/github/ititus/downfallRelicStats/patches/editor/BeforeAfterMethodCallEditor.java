package io.github.ititus.downfallRelicStats.patches.editor;

import javassist.CannotCompileException;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.List;

public class BeforeAfterMethodCallEditor extends SafeExprEditor {

    private final int requiredIndex;
    private final String requiredTargetClassName;
    private final String requiredTargetMethodName;
    private final String callbackClassName;
    private final boolean doBefore;
    private final boolean doAfter;
    private boolean addThis;
    private boolean addReceiver;
    private boolean addArgs;

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
        this.requiredIndex = requiredIndex;
        this.requiredTargetClassName = requiredTargetClassName;
        this.requiredTargetMethodName = requiredTargetMethodName;
        this.callbackClassName = callbackClassName;
        this.doBefore = doBefore;
        this.doAfter = doAfter;
    }

    public BeforeAfterMethodCallEditor addThis() {
        this.addThis = true;
        return this;
    }

    public BeforeAfterMethodCallEditor addReceiver() {
        this.addReceiver = true;
        return this;
    }

    public BeforeAfterMethodCallEditor addArgs() {
        this.addArgs = true;
        return this;
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
                List<String> args = new ArrayList<>();
                if (addThis) {
                    args.add("this");
                }
                if (addReceiver) {
                    args.add("$0");
                }
                if (addArgs) {
                    args.add("$$");
                }
                b.append(String.join(",", args));
                b.append(");");
            }
            b.append("$_=$proceed($$);");
            if (doAfter) {
                b.append(callbackClassName).append(".after(");
                List<String> args = new ArrayList<>();
                if (addThis) {
                    args.add("this");
                }
                if (addReceiver) {
                    args.add("$0");
                }
                if (addArgs) {
                    args.add("$$");
                }
                b.append(String.join(",", args));
                b.append(");");
            }
            m.replace(b.append('}').toString());
        }
    }
}
