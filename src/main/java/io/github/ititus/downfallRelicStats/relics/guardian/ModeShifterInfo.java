package io.github.ititus.downfallRelicStats.relics.guardian;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import guardian.GuardianMod;
import guardian.relics.ModeShifter;
import guardian.vfx.AddGemToStartingDeckEffect;
import io.github.ititus.downfallRelicStats.BaseCardRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.ConstructorHookEditor;
import javassist.expr.ExprEditor;

public final class ModeShifterInfo extends BaseCardRelicStats {

    private static final ModeShifterInfo INSTANCE = new ModeShifterInfo();

    private ModeShifterInfo() {
        super(ModeShifter.ID);
    }

    public static ModeShifterInfo getInstance() {
        return INSTANCE;
    }

    // TODO: this might break if GuardianMod starts to use auto card registering
    @SpirePatch(
            clz = GuardianMod.class,
            method = "receivePostCreateStartingDeck"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new ConstructorHookEditor(AddGemToStartingDeckEffect.class, Patch.class, 1);
        }

        public static void hook(AbstractCard gemCard) {
            getInstance().stats.setCard(gemCard);
        }
    }
}
