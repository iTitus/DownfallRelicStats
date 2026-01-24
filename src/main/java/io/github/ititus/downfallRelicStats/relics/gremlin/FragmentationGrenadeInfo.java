package io.github.ititus.downfallRelicStats.relics.gremlin;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import gremlin.actions.DamageRandomEnemyActionButItsRelicRng;
import gremlin.actions.PseudoDamageRandomEnemyAction;
import gremlin.patches.RandomDamagePatch;
import gremlin.relics.FragmentationGrenade;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.cards.EtherStep;
import theHexaghost.cards.GhostLash;

public final class FragmentationGrenadeInfo extends BaseCombatRelicStats {

    private static final FragmentationGrenadeInfo INSTANCE = new FragmentationGrenadeInfo();

    private FragmentationGrenadeInfo() {
        super(FragmentationGrenade.ID);
    }

    public static FragmentationGrenadeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatches({
            @SpirePatch(
                    clz = RandomDamagePatch.class,
                    method = "Postfix"
            ),
            @SpirePatch(
                    clz = PseudoDamageRandomEnemyAction.class,
                    method = "update"
            ),
            @SpirePatch(
                    clz = DamageRandomEnemyActionButItsRelicRng.class,
                    method = "update"
            ),
            @SpirePatch(
                    clz = EtherStep.class,
                    method = "afterlife"
            ),
            @SpirePatch(
                    clz = GhostLash.class,
                    method = "afterlife"
            )
    })
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractRelic.class, "flash", Patch.class, false, true);
        }

        public static void after() {
            getInstance().trigger();
        }
    }
}
