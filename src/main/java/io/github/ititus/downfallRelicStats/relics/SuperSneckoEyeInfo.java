package io.github.ititus.downfallRelicStats.relics;

import basemod.interfaces.OnStartBattleSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.SafeExprEditor;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import sneckomod.relics.SuperSneckoEye;

import java.text.DecimalFormat;
import java.util.IdentityHashMap;
import java.util.Map;

public final class SuperSneckoEyeInfo extends BaseRelicStats<SuperSneckoEyeInfo.Stats> implements OnStartBattleSubscriber {

    private static final SuperSneckoEyeInfo INSTANCE = new SuperSneckoEyeInfo();

    private SuperSneckoEyeInfo() {
        super(SuperSneckoEye.ID, Stats.class);
    }

    public static SuperSneckoEyeInfo getInstance() {
        return INSTANCE;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        stats.initialCosts = new IdentityHashMap<>();
        stats.activated = false;
    }

    public static class Stats implements StatContainer {

        int[] costDistribution;
        int totalDiscount;
        int activationCount;
        transient Map<AbstractCard, Integer> initialCosts;
        transient boolean activated;

        private int[] costDistribution() {
            if (costDistribution == null) {
                costDistribution = new int[4];
            }

            return costDistribution;
        }

        public void addCost(int cost) {
            if (cost < 0 || cost > 3) {
                throw new IllegalArgumentException();
            }

            costDistribution()[cost]++;
        }

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");

            StringBuilder b = new StringBuilder(description[0]);
            int totalCards = 0;
            for (int i = 0; i <= 3; i++) {
                int cards = costDistribution()[i];
                b.append(String.format(description[1], i)).append(cards);
                totalCards += cards;
            }

            b.append(description[2]).append(df.format((double) totalDiscount / Math.max(1, totalCards)));
            b.append(description[3]).append(activationCount);
            return b.toString();
        }
    }

    @SpirePatch(
            clz = ConfusionPower.class,
            method = "onCardDraw"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new SafeExprEditor() {

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(Random.class.getName()) && m.getMethodName().equals("random")) {
                        m.replace("{$_=$proceed($$);" + Patch.class.getName() + ".hook(card,$_);}");
                    }
                }
            };
        }

        // TODO: keep in sync with SuperSneckoEye#onCardDraw and SneckoInfo#patch
        public static void hook(AbstractCard card, int newCost) {
            if (AbstractDungeon.player.hasRelic(SuperSneckoEye.ID) && (card.type != AbstractCard.CardType.SKILL || !AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID))) {
                if (newCost == 3 && !getInstance().stats.activated) {
                    newCost = 0;
                    getInstance().stats.activationCount++;
                    getInstance().stats.activated = true;
                }

                getInstance().stats.addCost(newCost);

                int initialCost = getInstance().stats.initialCosts.computeIfAbsent(card, c -> c.cost);
                int discount = initialCost - newCost;
                getInstance().stats.totalDiscount += discount;
            }
        }
    }
}
