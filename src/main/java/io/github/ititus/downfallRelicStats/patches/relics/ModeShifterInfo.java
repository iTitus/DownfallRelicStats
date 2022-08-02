package io.github.ititus.downfallRelicStats.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import guardian.powers.ModeShiftPower;
import guardian.relics.ModeShifter;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class ModeShifterInfo extends BaseRelicStats<ModeShifterInfo.Stats> {

    private static final ModeShifterInfo INSTANCE = new ModeShifterInfo();
    private static final int DEFAULT_BLOCK_AMOUNT = ReflectionHacks.getPrivateStatic(ModeShiftPower.class, "BLOCKONTRIGGER");

    private ModeShifterInfo() {
        super(ModeShifter.ID, Stats.class);
    }

    public static ModeShifterInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats extends BaseCardRelicStats.Stats {

        int block = 0;

        @Override
        public String getDescription(String[] description) {
            return (getCard() != null ? super.getDescription(description) + " NL " : "") +
                    description[1] + block;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(extendedDescription, 0, block, totalTurns, totalCombats);
        }
    }

    @SpirePatch(
            clz = ModeShifter.class,
            method = "onEquip"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(ShowCardAndObtainEffect.class, Patch1.class, 1);
        }

        public static void hook(AbstractCard gemCard) {
            getInstance().stats.setCard(gemCard);
        }
    }

    @SpirePatch(
            clz = ModeShiftPower.class,
            method = "onSpecificTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(GainBlockAction.class, Patch2.class, 3);
        }

        public static void hook(int blockAmount) {
            getInstance().stats.block += blockAmount - DEFAULT_BLOCK_AMOUNT;
        }
    }
}
