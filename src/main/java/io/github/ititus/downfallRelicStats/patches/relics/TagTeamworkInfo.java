package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gremlin.relics.TagTeamwork;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;
import relicstats.actions.CardDrawFollowupAction;
import relicstats.actions.PreCardDrawAction;

public final class TagTeamworkInfo extends BaseCombatRelicStats {

    private static final TagTeamworkInfo INSTANCE = new TagTeamworkInfo();

    private TagTeamworkInfo() {
        super(TagTeamwork.ID);
    }

    public static TagTeamworkInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = TagTeamwork.class,
            method = "onTrigger"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, GameActionManager.class, "addToBottom", Patch.class);
        }

        public static void before(TagTeamwork __instance) {
            AbstractDungeon.actionManager.addToBottom(new PreCardDrawAction(getInstance()));
        }

        public static void after(TagTeamwork __instance) {
            AbstractDungeon.actionManager.addToBottom(new CardDrawFollowupAction(getInstance()));
        }
    }
}
