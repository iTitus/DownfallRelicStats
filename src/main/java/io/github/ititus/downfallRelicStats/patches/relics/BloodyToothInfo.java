package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.BloodyTooth;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.BeforeAfterMethodCallEditor;
import io.github.ititus.downfallRelicStats.StatContainer;
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

        @Override
        public String getDescription(String[] description) {
            StringBuilder b = new StringBuilder();
            int allElites = elites != null ? elites.stream().mapToInt(i -> i).sum() : 0;
            b.append(description[0]).append(allElites);

            if (elites != null && !elites.isEmpty()) {
                for (int i = 0; i < elites.size() - 1; i++) {
                    b.append(String.format(description[1], i + 1)).append(elites.get(i));
                }
            } else {
                b.append(String.format(description[1], 1)).append(0);
            }

            return b.toString();
        }
    }

    @SpirePatch(
            clz = BloodyTooth.class,
            method = "onVictory"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(0, BloodyTooth.class, "flash", Patch.class, false, true, false);
        }

        public static void after() {
            List<Integer> elites = getInstance().stats.elites;
            if (elites == null) {
                getInstance().stats.elites = elites = new ArrayList<>();
            }

            int size = elites.size();
            int act = AbstractDungeon.actNum;
            if (size <= act) {
                elites.addAll(IntStream.rangeClosed(size, act).map(i -> 0).boxed().collect(Collectors.toList()));
            }

            elites.set(act, elites.get(act) + 1);
        }
    }
}
