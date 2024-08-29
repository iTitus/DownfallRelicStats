package io.github.ititus.downfallRelicStats.patches.editor;

import javassist.CannotCompileException;
import javassist.expr.NewExpr;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConstructorHookEditor extends SafeExprEditor {

    private final String targetClassName;
    private final String callbackClassName;
    private final int[] parameters;

    public ConstructorHookEditor(Class<?> targetClass, Class<?> callbackClass, int... parameters) {
        this(targetClass.getName(), callbackClass.getName(), parameters);
    }

    public ConstructorHookEditor(String targetClassName, String callbackClassName, int... parameters) {
        this.targetClassName = targetClassName;
        this.callbackClassName = callbackClassName;
        this.parameters = Arrays.copyOf(parameters, parameters.length);
    }

    @Override
    public void edit(NewExpr e) throws CannotCompileException {
        if (e.getClassName().equals(targetClassName)) {
            e.replace("{$_=$proceed($$);" + callbackClassName + ".hook(" + Arrays.stream(parameters).mapToObj(i -> "$" + i).collect(Collectors.joining(",")) + ");}");
        }
    }
}
