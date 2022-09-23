package io.github.ititus.downfallRelicStats.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import sneckomod.relics.SneckoBoss;
import sneckomod.relics.SneckoCommon;

import java.util.Objects;

public final class SneckoCommonInfo extends BaseRelicStats<SneckoCommonInfo.Stats> {

    private static final SneckoCommonInfo INSTANCE = new SneckoCommonInfo();

    private SneckoCommonInfo() {
        super(SneckoCommon.ID, Stats.class);
    }

    public static SneckoCommonInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        String cardColor;

        @Override
        public String getDescription(String[] description) {
            if (cardColor == null) {
                return description[0] + description[1];
            }

            AbstractCard.CardColor colorEnumValue = null;
            try {
                colorEnumValue = AbstractCard.CardColor.valueOf(cardColor);
            } catch (IllegalArgumentException ignored) {
            }

            String className = null;
            if (colorEnumValue != null) {
                for (AbstractPlayer character : CardCrawlGame.characterManager.getAllCharacters()) {
                    if (character != null && colorEnumValue.equals(character.getCardColor())) {
                        className = character.getLocalizedCharacterName();
                        break;
                    }
                }
            }

            if (className != null) {
                return description[0] + className;
            } else {
                return description[0] + cardColor;
            }
        }

        public void setColor(AbstractCard.CardColor cardColor) {
            this.cardColor = Objects.requireNonNull(cardColor, "cardColor").name();
        }
    }

    @SpirePatch(
            clz = SneckoCommon.class,
            method = "onEquip"
    )
    @SuppressWarnings("unused")
    public static class Patch1 {

        public static void Postfix() {
            if (SneckoBoss.myColor != null && AbstractDungeon.player.hasRelic(SneckoBoss.ID)) {
                getInstance().stats.setColor(SneckoBoss.myColor);
            }
        }
    }

    @SpirePatch(
            clz = SneckoCommon.class,
            method = "update"
    )
    @SuppressWarnings("unused")
    public static class Patch2 {

        public static ExprEditor Instrument() {

            return new ExprEditor() {

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(SneckoBoss.class.getName()) && f.getFieldName().equals("myColor") && f.isWriter()) {
                        f.replace("{$_=$proceed($$);" + Patch2.class.getName() + ".hook($1);}");
                    }
                }
            };
        }

        public static void hook(AbstractCard.CardColor color) {
            getInstance().stats.setColor(color);
        }
    }
}
