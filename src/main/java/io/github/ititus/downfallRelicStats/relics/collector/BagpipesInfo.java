package io.github.ititus.downfallRelicStats.relics.collector;

import collector.relics.Bagpipes;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.FieldAccessHookEditor;
import javassist.expr.ExprEditor;

public final class BagpipesInfo extends BaseCombatRelicStats {

    private static final BagpipesInfo INSTANCE = new BagpipesInfo();

    private BagpipesInfo() {
        super(Bagpipes.ID);
    }

    public static BagpipesInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Bagpipes.class,
            method = "onApplyPower"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new FieldAccessHookEditor(1, AbstractPower.class, "amount", Patch.class);
        }

        public static int hook(AbstractPower __instance, int amount) {
            // TODO: do not count when target has artifact
            getInstance().registerStartingAmount(__instance.amount);
            getInstance().registerEndingAmount(amount);
            return amount;
        }
    }
}
