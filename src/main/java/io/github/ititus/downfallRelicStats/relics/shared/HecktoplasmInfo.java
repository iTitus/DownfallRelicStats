package io.github.ititus.downfallRelicStats.relics.shared;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import downfall.relics.Hecktoplasm;
import downfall.ui.campfire.BustKeyOption;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

public final class HecktoplasmInfo extends BaseCombatRelicStats {

    private static final HecktoplasmInfo INSTANCE = new HecktoplasmInfo();
    private static final int KEY_BUST_COST = 50;

    private HecktoplasmInfo() {
        super(Hecktoplasm.ID);
        this.showPerTurn = false;
        this.showPerCombat = false;
    }

    public static HecktoplasmInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "gainGold"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Prefix(AbstractPlayer __instance, int amount) {
            if (__instance.hasRelic(Hecktoplasm.ID) && amount > 0) {
                getInstance().increaseAmount(amount);
            }
        }
    }

    @SpirePatch(
            clz = BustKeyOption.class,
            method = "useOption"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "loseGold", Patch2.class, false, true);
        }

        public static void after() {
            if (AbstractDungeon.player.hasRelic(Hecktoplasm.ID)) {
                getInstance().increaseAmount(-KEY_BUST_COST);
            }
        }
    }
}
