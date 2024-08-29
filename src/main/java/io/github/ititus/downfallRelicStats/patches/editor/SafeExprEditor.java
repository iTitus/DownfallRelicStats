package io.github.ititus.downfallRelicStats.patches.editor;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.MethodInfo;
import javassist.expr.ExprEditor;

public class SafeExprEditor extends ExprEditor {

    private final Throwable exception;

    public SafeExprEditor() {
        super();
        this.exception = new RuntimeException("expr editor creation");
    }

    @Override
    public boolean doit(CtClass clazz, MethodInfo minfo) throws CannotCompileException {
        if (!super.doit(clazz, minfo)) {
            throw new IllegalStateException("could not patch with " + this, this.exception);
        }

        return true;
    }
}
