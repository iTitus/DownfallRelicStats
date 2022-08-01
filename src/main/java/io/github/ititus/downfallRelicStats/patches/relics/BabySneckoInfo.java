package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;
import sneckomod.actions.BabySneckoAttackAction;
import sneckomod.relics.BabySnecko;

public final class BabySneckoInfo extends BaseCombatRelicStats {

    private static final BabySneckoInfo INSTANCE = new BabySneckoInfo();

    private BabySneckoInfo() {
        super(BabySnecko.ID);
    }

    public static BabySneckoInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = BabySneckoAttackAction.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static PreAoeDamageAction preAction;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before(BabySneckoAttackAction __instance) {
            AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        }

        public static void after(BabySneckoAttackAction __instance) {
            AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        }
    }
}
