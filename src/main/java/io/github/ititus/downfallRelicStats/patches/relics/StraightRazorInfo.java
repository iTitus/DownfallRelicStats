package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hermit.relics.StraightRazor;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import io.github.ititus.downfallRelicStats.patches.editor.BeforeAfterMethodCallEditor;
import javassist.expr.ExprEditor;

import java.text.DecimalFormat;

public final class StraightRazorInfo extends BaseRelicStats<StraightRazorInfo.Stats> {

    private static final StraightRazorInfo INSTANCE = new StraightRazorInfo();

    private StraightRazorInfo() {
        super(StraightRazor.ID, Stats.class);
    }

    public static StraightRazorInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int removes = 0;
        int healed = 0;

        @Override
        public String getDescription(String[] description) {
            DecimalFormat df = new DecimalFormat("#.###");
            return description[0] + removes +
                    description[1] + healed +
                    description[2] + df.format((double) healed / Math.max(1, removes));
        }
    }

    @SpirePatch(
            clz = StraightRazor.class,
            method = "onRemoveCardFromMasterDeck"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        private static int hp;

        public static ExprEditor Instrument() {
            return new BeforeAfterMethodCallEditor(AbstractPlayer.class, "heal", Patch.class);
        }

        public static void before() {
            hp = AbstractDungeon.player.currentHealth;
        }

        public static void after() {
            getInstance().stats.removes++;
            getInstance().stats.healed += AbstractDungeon.player.currentHealth - hp;
        }
    }
}
