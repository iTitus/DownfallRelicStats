package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.BlackPowder;
import io.github.ititus.downfallRelicStats.DownfallRelicStats;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import relicstats.AmountIncreaseCallback;
import relicstats.CombatStatsInfo;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

import java.util.ArrayList;
import java.util.Arrays;

@SpirePatch(
        clz = BlackPowder.class,
        method = "onPlayerEndTurn"
)
public final class BlackPowderInfo extends CombatStatsInfo implements AmountIncreaseCallback {

    private static final String statId = getLocId("BlackPowder");
    private static final String[] description = CardCrawlGame.languagePack.getUIString(statId).TEXT;
    private static final BlackPowderInfo INSTANCE = new BlackPowderInfo();

    private static PreAoeDamageAction preAction;

    private BlackPowderInfo() {
    }

    public static BlackPowderInfo getInstance() {
        return INSTANCE;
    }

    @SpireInsertPatch(locator = BlackPowderInfo.Locator1.class)
    public static void before(BlackPowder __instance) {
        AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
    }

    @SpireInsertPatch(locator = BlackPowderInfo.Locator2.class)
    public static void after(BlackPowder __instance) {
        AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
    }

    @Override
    public String getBaseDescription() {
        return description[0];
    }

    @Override
    public void increaseAmount(int amount) {
        this.amount += amount;
    }

    private static final class Locator1 extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher matcher = new Matcher.MethodCallMatcher(BlackPowder.class, "flash");
            int[] locs = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            DownfallRelicStats.LOGGER.info("[Loc1] " + Arrays.toString(locs));
            return locs;
        }
    }

    private static final class Locator2 extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher matcher = new Matcher.FieldAccessMatcher(BlackPowder.class, "PowderCharge");
            int[] locs = LineFinder.findAllInOrder(ctMethodToPatch, matcher);
            if (locs.length != 3) {
                throw new AssertionError("patch error");
            }

            return new int[] { locs[locs.length - 1] };
        }
    }
}
