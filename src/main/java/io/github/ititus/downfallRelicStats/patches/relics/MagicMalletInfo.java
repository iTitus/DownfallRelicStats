package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import gremlin.powers.WizPower;
import gremlin.relics.MagicalMallet;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class MagicMalletInfo extends BaseCombatRelicStats {

    private static final MagicMalletInfo INSTANCE = new MagicMalletInfo();

    private MagicMalletInfo() {
        super(MagicalMallet.ID);
    }

    public static MagicMalletInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = MagicalMallet.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(WizPower.class, Patch.class, 2);
        }

        public static void hook(int amount) {
            getInstance().increaseAmount(amount);
        }
    }
}
