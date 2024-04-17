package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import downfall.cards.KnowingSkullWish;
import downfall.relics.KnowingSkull;
import io.github.ititus.downfallRelicStats.*;
import io.github.ititus.downfallRelicStats.actions.PlayerDamageFollowAction;
import io.github.ititus.downfallRelicStats.actions.PrePlayerDamageAction;
import javassist.expr.ExprEditor;
import sneckomod.actions.ChangeGoldAction;

import java.text.DecimalFormat;

public final class KnowingSkullInfo extends BaseRelicStats<KnowingSkullInfo.Stats> {

    private static final KnowingSkullInfo INSTANCE = new KnowingSkullInfo();

    private KnowingSkullInfo() {
        super(KnowingSkull.ID, Stats.class);
    }

    public static KnowingSkullInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int hp;
        int gold;
        int goldTimes;
        int cards;
        int potions;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#%");
            int total = goldTimes + cards + potions;
            return description[0] + total +
                    description[1] + hp +
                    description[2] + gold +
                    description[3] + cards +
                    description[4] + potions +
                    String.format(description[5], df.format((double) goldTimes / Math.max(1, total)), df.format((double) cards / Math.max(1, total)), df.format((double) potions / Math.max(1, total)));
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return BaseCombatRelicStats.generateExtendedDescription(description, 6, goldTimes + cards + potions, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 7, hp, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 8, gold, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 9, cards, 0, totalCombats) +
                    BaseCombatRelicStats.generateExtendedDescription(description, 10, potions, 0, totalCombats);
        }
    }

    @SpirePatch(
            clz = KnowingSkullWish.class,
            method = "doChoiceStuff"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        private static PrePlayerDamageAction preAction;

        @SpireInstrumentPatch
        public static ExprEditor Instrument1() {
            return new BeforeAfterMultiMethodCallEditor(KnowingSkullWish.class, "atb", Patch1.class);
        }

        @SpireInstrumentPatch
        public static ExprEditor Instrument2() {
            return new ConstructorHookEditor(ChangeGoldAction.class, Patch1.class, 1);
        }

        public static void before(int n) {
            if (n == 0 || n == 2 || n == 4) {
                AbstractDungeon.actionManager.addToBottom(preAction = new PrePlayerDamageAction());
            } else if (n != 1 && n != 3 && n != 5) {
                throw new AssertionError();
            }
        }

        public static void after(int n) {
            if (n == 0 || n == 2 || n == 4) {
                AbstractDungeon.actionManager.addToBottom(new PlayerDamageFollowAction(by -> getInstance().stats.hp += by, preAction));
            } else if (n == 1) {
                // TODO: track gold amount
                getInstance().stats.goldTimes++;
            } else if (n == 3) {
                getInstance().stats.cards++;
            } else if (n == 5) {
                getInstance().stats.potions++;
            } else {
                throw new AssertionError();
            }
        }

        public static void hook(int goldAmount) {
            getInstance().stats.gold += goldAmount;
        }
    }
}
