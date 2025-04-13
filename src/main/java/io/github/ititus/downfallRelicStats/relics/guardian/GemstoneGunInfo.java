package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import guardian.relics.GemstoneGun;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.SafeExprEditor;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class GemstoneGunInfo extends BaseMultiCardRelicStats {

    private static final GemstoneGunInfo INSTANCE = new GemstoneGunInfo();

    private GemstoneGunInfo() {
        super(GemstoneGun.ID);
    }

    public static GemstoneGunInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = GemstoneGun.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            // TODO: implement a MultiFieldAccessHookEditor
            return new SafeExprEditor() {

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(GemstoneGun.class.getName()) && f.getFieldName().startsWith("myGem")) {
                        if (f.isReader() || !f.isWriter()) {
                            throw new AssertionError();
                        }

                        f.replace("{$proceed(" + Patch.class.getName() + ".hook($1));}");
                    }
                }
            };
        }

        public static String hook(String cardId) {
            getInstance().stats.addCard(cardId);
            return cardId;
        }
    }
}
