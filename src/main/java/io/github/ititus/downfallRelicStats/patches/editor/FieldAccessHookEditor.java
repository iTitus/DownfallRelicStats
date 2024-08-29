package io.github.ititus.downfallRelicStats.patches.editor;

import javassist.CannotCompileException;
import javassist.expr.FieldAccess;

public class FieldAccessHookEditor extends SafeExprEditor {

    private final int requiredIndex;
    private final String requiredTargetClassName;
    private final String requiredTargetFieldName;
    private final String callbackClassName;

    private int n;

    public FieldAccessHookEditor(Class<?> requiredTargetClass, String requiredTargetFieldName, Class<?> callbackClass) {
        this(requiredTargetClass.getName(), requiredTargetFieldName, callbackClass.getName());
    }

    public FieldAccessHookEditor(int requiredIndex, Class<?> requiredTargetClass, String requiredTargetFieldName, Class<?> callbackClass) {
        this(requiredIndex, requiredTargetClass.getName(), requiredTargetFieldName, callbackClass.getName());
    }

    public FieldAccessHookEditor(String requiredTargetClassName, String requiredTargetFieldName, String callbackClassName) {
        this(0, requiredTargetClassName, requiredTargetFieldName, callbackClassName);
    }

    public FieldAccessHookEditor(int requiredIndex, String requiredTargetClassName, String requiredTargetFieldName, String callbackClassName) {
        this.requiredIndex = requiredIndex;
        this.requiredTargetClassName = requiredTargetClassName;
        this.requiredTargetFieldName = requiredTargetFieldName;
        this.callbackClassName = callbackClassName;
    }

    @Override
    public void edit(FieldAccess f) throws CannotCompileException {
        if (f.getClassName().equals(requiredTargetClassName) && f.getFieldName().equals(requiredTargetFieldName)) {
            if (n++ != requiredIndex) {
                return;
            }

            StringBuilder b = new StringBuilder().append('{');
            if (f.isReader()) {
                if (f.isWriter()) {
                    throw new AssertionError();
                }
                b.append("$_=").append(callbackClassName).append(".hook(");
                if (!f.isStatic()) {
                    b.append("$0, ");
                }
                b.append("$proceed($$));");
            } else if (f.isWriter()) {
                if (f.isReader()) {
                    throw new AssertionError();
                }
                b.append("$proceed(").append(callbackClassName).append(".hook(");
                if (!f.isStatic()) {
                    b.append("$0, ");
                }
                b.append("$1));");
            } else {
                throw new AssertionError();
            }

            f.replace(b.append('}').toString());
        }
    }
}
