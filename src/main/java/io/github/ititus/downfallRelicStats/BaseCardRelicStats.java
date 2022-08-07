package io.github.ititus.downfallRelicStats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;

public abstract class BaseCardRelicStats extends BaseRelicStats<BaseCardRelicStats.Stats> {

    protected BaseCardRelicStats(String relicId) {
        super(relicId, Stats.class);
    }

    public static String cardToString(String cardIdOrMetricId) {
        AbstractCard cardObj = CardLibrary.getCard(cardIdOrMetricId.split("\\+")[0]);
        String rarityColor = null;
        if (cardObj != null) {
            switch (cardObj.rarity) {
                case UNCOMMON:
                    rarityColor = "b";
                    break;
                case RARE:
                    rarityColor = "y";
                    break;
            }
        }

        String cardName = CardLibrary.getCardNameFromMetricID(cardIdOrMetricId);
        if (rarityColor != null) {
            return FontHelper.colorString(cardName, rarityColor);
        } else {
            return cardName;
        }
    }

    public static class Stats implements StatContainer {

        private String card = null;
        private transient String cachedName = null;

        public String getCard() {
            return card;
        }

        public void setCard(String cardIdOrMetricId) {
            this.card = cardIdOrMetricId;
            this.cachedName = null;
        }

        public void setCard(AbstractCard card) {
            setCard(card != null ? card.getMetricID() : null);
        }

        public void setCardWithoutUpgrade(AbstractCard card) {
            setCard(card != null ? card.cardID : null);
        }

        @Override
        public String getDescription(String[] description) {
            if (card == null) {
                return "";
            }

            if (cachedName == null) {
                cachedName = description[0] + cardToString(card);
            }

            return cachedName;
        }
    }
}
