package io.github.ititus.downfallRelicStats.relics.champ;

import champ.relics.RageAmulet;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.StrengthPower;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class RageAmuletInfo extends BaseCombatRelicStats {

    private static final RageAmuletInfo INSTANCE = new RageAmuletInfo();

    private RageAmuletInfo() {
        super(RageAmulet.ID);
    }

    public static RageAmuletInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = RageAmulet.class,
            method = "addNextTurnPower"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(StrengthPower.class, Patch.class, 2);
        }

        public static void hook(int strengthAmount) {
            getInstance().increaseAmount(strengthAmount);
        }
    }
}
