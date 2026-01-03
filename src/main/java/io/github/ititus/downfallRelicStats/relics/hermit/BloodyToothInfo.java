package io.github.ititus.downfallRelicStats.relics.hermit;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.BloodyTooth;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class BloodyToothInfo extends BaseRelicStats<BloodyToothInfo.Stats> {

    private static final BloodyToothInfo INSTANCE = new BloodyToothInfo();

    private BloodyToothInfo() {
        super(BloodyTooth.ID, Stats.class);
    }

    public static BloodyToothInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        List<Integer> elites = new ArrayList<>();
        int hp = 0;
        int gold = 0;

        @Override
        public String getDescription(String[] description) {
            StringBuilder b = new StringBuilder();

            int allElites = elites != null ? elites.stream().mapToInt(i -> i).sum() : 0;
            b.append(description[0]).append(allElites);

            int len = Math.max(1, Math.max(
                    elites != null ? elites.size() : 0,
                    CardCrawlGame.isInARun() ? AbstractDungeon.actNum : 0
            ));
            for (int i = 0; i < len; i++) {
                int elitesFought = elites == null || i >= elites.size() ? 0 : elites.get(i);
                b.append(description[1]).append(i + 1).append(description[2]).append(elitesFought);
            }

            b.append(description[3]).append(hp);
            b.append(description[4]).append(gold);
            return b.toString();
        }
    }

    @SpirePatch(
            clz = BloodyTooth.class,
            method = "onVictory"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(BloodyTooth.class, "flash", Patch1.class, false, true);
        }

        public static void after() {
            List<Integer> elites = getInstance().stats.elites;
            if (elites == null) {
                getInstance().stats.elites = elites = new ArrayList<>();
            }

            int size = elites.size();
            int act = AbstractDungeon.actNum - 1;
            if (act >= size) {
                elites.addAll(IntStream.rangeClosed(size, act).map(i -> 0).boxed().collect(Collectors.toList()));
            }

            elites.set(act, elites.get(act) + 1);
        }
    }

    @SpirePatch(
            clz = BloodyTooth.class,
            method = "onVictory"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        private static int hp;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "heal", Patch2.class);
        }

        public static void before() {
            hp = AbstractDungeon.player.currentHealth;
        }

        public static void after() {
            getInstance().stats.hp += AbstractDungeon.player.currentHealth - hp;
        }
    }

    @SpirePatch(
            clz = BloodyTooth.class,
            method = "onVictory"
    )
    @SuppressWarnings("unused")
    public static class Patch3 {

        private static int gold;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "gainGold", Patch3.class);
        }

        public static void before() {
            gold = AbstractDungeon.player.gold;
        }

        public static void after() {
            getInstance().stats.gold += AbstractDungeon.player.gold - gold;
        }
    }
}
