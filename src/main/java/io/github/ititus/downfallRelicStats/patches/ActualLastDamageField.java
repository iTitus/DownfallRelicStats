package io.github.ititus.downfallRelicStats.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = SpirePatch.CLASS
)
public class ActualLastDamageField {

    public static final SpireField<Integer> actualLastDamageTaken = new SpireField<>(() -> 0);

    public static int get(AbstractPlayer __instance) {
        return ActualLastDamageField.actualLastDamageTaken.get(__instance);
    }

    public static void set(AbstractPlayer __instance, int actualLastDamageTaken) {
        ActualLastDamageField.actualLastDamageTaken.set(__instance, actualLastDamageTaken);
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez = { DamageInfo.class }
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static int hp;

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                int n = 0;

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(AbstractPlayer.class.getName()) && f.getFieldName().equals("currentHealth") && !f.isStatic() && f.isWriter() && n++ == 0) {
                        f.replace("{" + Patch.class.getName() + ".before(this);$_=$proceed($$);" + Patch.class.getName() + ".after(this);}");
                    }
                }
            };
        }

        public static void before(AbstractPlayer __instance) {
            hp = Math.max(0, __instance.currentHealth);
        }

        public static void after(AbstractPlayer __instance) {
            int actualLastDamageTaken = hp - Math.max(0, __instance.currentHealth);
            ActualLastDamageField.actualLastDamageTaken.set(__instance, actualLastDamageTaken);
        }
    }
}
