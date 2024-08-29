package io.github.ititus.downfallRelicStats.patches.relics;

import collector.relics.FuelCanister;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.FieldAccessHookEditor;
import javassist.expr.ExprEditor;

public final class FuelCanisterInfo extends BaseCombatRelicStats {

    private static final FuelCanisterInfo INSTANCE = new FuelCanisterInfo();

    private FuelCanisterInfo() {
        super(FuelCanister.ID);
    }

    public static FuelCanisterInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = FuelCanister.class,
            method = "onPlayerEndTurn"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new FieldAccessHookEditor(AbstractCard.class, "retain", Patch.class);
        }

        public static boolean hook(AbstractCard __instance, boolean retain) {
            if (retain && !__instance.retain) {
                getInstance().increaseAmount(1);
            }
            return retain;
        }
    }
}
