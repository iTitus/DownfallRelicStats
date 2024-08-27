package io.github.ititus.downfallRelicStats.patches.relics.hexaghost;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import theHexaghost.ghostflames.InfernoGhostflame;
import theHexaghost.relics.IceCube;

public final class IceCubeInfo extends BaseCombatRelicStats {

    private static final IceCubeInfo INSTANCE = new IceCubeInfo();
    // TODO: keep in sync with InfernoGhostflame#onCharge
    private static final int INTENSITY_LOSS = 2;

    private IceCubeInfo() {
        super(IceCube.ID);
    }

    public static IceCubeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = InfernoGhostflame.class,
            method = "onCharge"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "hasRelic", Patch.class, false, true);
        }

        public static void after() {
            if (AbstractDungeon.player.hasRelic(IceCube.ID)) {
                getInstance().increaseAmount(INTENSITY_LOSS);
            }
        }
    }
}
