package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.BlackPowder;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.actions.AoeDamageFollowupAction;
import relicstats.actions.PreAoeDamageAction;

import java.util.ArrayList;

@SpirePatch(
        clz = BlackPowder.class,
        method = "onPlayerEndTurn"
)
@SuppressWarnings("unused")
public final class BlackPowderInfo extends BaseCombatRelicStats {

    private static final Logger LOGGER = LogManager.getLogger(BlackPowderInfo.class.getName());
    private static final BlackPowderInfo INSTANCE = new BlackPowderInfo();

    private static PreAoeDamageAction preAction;

    private BlackPowderInfo() {
        super(BlackPowder.ID);
    }

    public static BlackPowderInfo getInstance() {
        return INSTANCE;
    }

    @SpireInsertPatch(locator = BlackPowderInfo.Locator1.class)
    public static void before(BlackPowder __instance) {
        AbstractDungeon.actionManager.addToBottom(preAction = new PreAoeDamageAction());
        LOGGER.debug("added PreAoeDamageAction, actions={}", AbstractDungeon.actionManager.actions);
    }

    @SpireInsertPatch(locator = BlackPowderInfo.Locator2.class)
    public static void after(BlackPowder __instance) {
        AbstractDungeon.actionManager.addToBottom(new AoeDamageFollowupAction(getInstance(), preAction));
        LOGGER.debug("added AoeDamageFollowupAction, actions={}", AbstractDungeon.actionManager.actions);
    }

    private static final class Locator1 extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher matcher = new Matcher.MethodCallMatcher(BlackPowder.class, "addToBot");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
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
