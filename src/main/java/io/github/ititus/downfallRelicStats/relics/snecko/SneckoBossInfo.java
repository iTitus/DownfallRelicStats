package io.github.ititus.downfallRelicStats.relics.snecko;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.patches.editor.FieldAccessHookEditor;
import io.github.ititus.downfallRelicStats.stats.CardColorStats;
import javassist.expr.ExprEditor;
import sneckomod.relics.SneckoBoss;

public final class SneckoBossInfo extends BaseRelicStats<CardColorStats> {

    private static final SneckoBossInfo INSTANCE = new SneckoBossInfo();

    private SneckoBossInfo() {
        super(SneckoBoss.ID, CardColorStats.class);
    }

    public static SneckoBossInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = SneckoBoss.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static ExprEditor Instrument() {
            return new FieldAccessHookEditor(SneckoBoss.class, "myColor", Patch.class);
        }

        public static AbstractCard.CardColor hook(AbstractCard.CardColor color) {
            getInstance().stats.setColor(color);
            return color;
        }
    }
}
