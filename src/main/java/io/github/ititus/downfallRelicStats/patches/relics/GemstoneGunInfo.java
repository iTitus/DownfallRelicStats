package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import guardian.relics.GemstoneGun;
import io.github.ititus.downfallRelicStats.BaseMultiCardRelicStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class GemstoneGunInfo extends BaseMultiCardRelicStats {

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
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(GemstoneGun.class.getName()) && f.getFieldName().startsWith("myGem")) {
                        f.replace("{$_=$proceed($$);" + Patch.class.getName() + ".hook($1);}");
                    }
                }
            };
        }

        public static void hook(String cardId) {
            getInstance().stats.addCard(cardId);
        }
    }
}
