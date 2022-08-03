package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import downfall.relics.ExtraCursedKey;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class ExtraCursedKeyInfo extends BaseCombatRelicStats {

    private static final ExtraCursedKeyInfo INSTANCE = new ExtraCursedKeyInfo();

    private ExtraCursedKeyInfo() {
        super(ExtraCursedKey.ID);
        this.showPerTurn = false;
        this.showPerCombat = false;
    }

    public static ExtraCursedKeyInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = ExtraCursedKey.class,
            method = "onChestOpen"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(ShowCardAndObtainEffect.class, Patch.class);
        }

        public static void hook() {
            getInstance().increaseAmount(1);
        }
    }
}
