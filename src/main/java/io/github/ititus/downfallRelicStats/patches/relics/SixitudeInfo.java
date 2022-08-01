package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;
import theHexaghost.relics.Sixitude;

public final class SixitudeInfo extends BaseCombatRelicStats {

    private static final SixitudeInfo INSTANCE = new SixitudeInfo();

    private SixitudeInfo() {
        super(Sixitude.ID);
    }

    public static SixitudeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Sixitude.class,
            method = "onUseCard"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(1, Sixitude.class, "addToBot", Patch.class);
        }

        public static void before(Sixitude __instance) {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        }

        public static void after(Sixitude __instance) {
            AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        }
    }
}
