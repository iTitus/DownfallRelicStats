package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.FieldAccessHookEditor;
import javassist.expr.ExprEditor;
import sneckomod.relics.SneckoBoss;
import sneckomod.relics.SneckoCommon;

public final class SneckoBossInfo extends BaseRelicStats<SneckoCommonInfo.Stats> {

    private static final SneckoBossInfo INSTANCE = new SneckoBossInfo();

    private SneckoBossInfo() {
        super(SneckoBoss.ID, SneckoCommonInfo.Stats.class);
    }

    public static SneckoBossInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SneckoBoss.class,
            method = "onEquip"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Postfix() {
            if (SneckoBoss.myColor != null && AbstractDungeon.player.hasRelic(SneckoCommon.ID)) {
                getInstance().stats.setColor(SneckoBoss.myColor);
            }
        }
    }

    @SpirePatch(
            clz = SneckoBoss.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {
            return new FieldAccessHookEditor(SneckoBoss.class, "myColor", Patch2.class);
        }

        public static AbstractCard.CardColor hook(AbstractCard.CardColor color) {
            getInstance().stats.setColor(color);
            return color;
        }
    }
}
