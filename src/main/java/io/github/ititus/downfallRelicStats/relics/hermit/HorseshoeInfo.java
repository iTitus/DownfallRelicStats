package io.github.ititus.downfallRelicStats.relics.hermit;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import hermit.relics.Horseshoe;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.FieldAccessHookEditor;
import javassist.expr.ExprEditor;

public final class HorseshoeInfo extends BaseCombatRelicStats {

    private static final HorseshoeInfo INSTANCE = new HorseshoeInfo();

    private HorseshoeInfo() {
        super(Horseshoe.ID);
    }

    public static HorseshoeInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = Horseshoe.class,
            method = "onReceivePower"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new FieldAccessHookEditor(1, AbstractPower.class, "amount", Patch1.class);
        }

        public static int hook(AbstractPower __instance, int amount) {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Horseshoe.ID) && !AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID)) {
                getInstance().registerStartingAmount(Math.max(0, __instance.amount));
                getInstance().registerEndingAmount(Math.max(0, amount));
            }
            return amount;
        }
    }

    @SpirePatch(
            clz = Horseshoe.class,
            method = "onReceivePowerStacks"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static int Postfix(int __result, Horseshoe __instance, AbstractPower power, AbstractCreature source, int stackAmount) {
            if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Horseshoe.ID) && !AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID)) {
                getInstance().registerStartingAmount(Math.max(0, stackAmount));
                getInstance().registerEndingAmount(Math.max(0, __result));
            }
            return __result;
        }
    }
}
