package io.github.ititus.downfallRelicStats.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import gremlin.actions.PseudoDamageRandomEnemyAction;
import gremlin.patches.RandomDamagePatch;
import gremlin.relics.FragmentationGrenade;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class FragmentationGrenadeInfo extends BaseCombatRelicStats {

    private static final FragmentationGrenadeInfo INSTANCE = new FragmentationGrenadeInfo();

    private FragmentationGrenadeInfo() {
        super(FragmentationGrenade.ID);
    }

    public static FragmentationGrenadeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RandomDamagePatch.class,
            method = "Postfix"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch1.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }

    @SpirePatch(
            clz = PseudoDamageRandomEnemyAction.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch2.class, false, true);
        }

        public static void after() {
            getInstance().increaseAmount(1);
        }
    }
}
