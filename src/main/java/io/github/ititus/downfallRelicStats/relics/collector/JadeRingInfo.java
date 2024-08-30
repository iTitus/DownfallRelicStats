package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.JadeRing;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.SafeExprEditor;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public final class JadeRingInfo extends BaseCombatRelicStats {

    private static final JadeRingInfo INSTANCE = new JadeRingInfo();

    private JadeRingInfo() {
        super(JadeRing.ID);
    }

    public static JadeRingInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = JadeRing.class,
            method = "onMonsterDeath"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new SafeExprEditor() {

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("gainGold")) {
                        m.replace("{" + Patch.class.getName() + ".before($0);$_=$proceed($$);" + Patch.class.getName() + ".after($0);}");
                    }
                }
            };
        }

        public static void before(AbstractPlayer __instance) {
            getInstance().registerStartingAmount(__instance.gold);
        }

        public static void after(AbstractPlayer __instance) {
            getInstance().registerEndingAmount(__instance.gold);
        }
    }
}
