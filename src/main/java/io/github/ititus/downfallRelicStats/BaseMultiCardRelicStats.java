package io.github.ititus.downfallRelicStats;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseMultiCardRelicStats extends BaseRelicStats<BaseMultiCardRelicStats.Stats> {

    protected BaseMultiCardRelicStats(String relicId) {
        super(relicId, Stats.class);
    }

    public static class Stats implements StatContainer {

        private List<String> cards = new ArrayList<>();
        private transient String cachedNames = null;

        private List<String> getCardsInternal() {
            if (this.cards == null) {
                this.cards = new ArrayList<>();
            }

            return this.cards;
        }

        public List<String> getCards() {
            return Collections.unmodifiableList(getCardsInternal());
        }

        public void setCards(List<String> cards) {
            this.cards = cards == null ? new ArrayList<>() : new ArrayList<>(cards);
            this.cachedNames = null;
        }

        public void clearCards() {
            setCards(null);
        }

        public void addCard(String cardIdOrMetricId) {
            if (cardIdOrMetricId != null) {
                getCardsInternal().add(cardIdOrMetricId);
                this.cachedNames = null;
            }
        }

        public void addCard(AbstractCard card) {
            if (card != null) {
                addCard(card.getMetricID());
            }
        }

        public void addCardWithoutUpgrade(AbstractCard card) {
            if (card != null) {
                addCard(card.cardID);
            }
        }

        @Override
        public String getDescription(String[] description) {
            if (cachedNames == null) {
                StringBuilder b = new StringBuilder(description[0]);
                List<String> cards = getCardsInternal();
                if (cards.isEmpty()) {
                    b.append(description[1]);
                } else {
                    for (String card : cards) {
                        b.append(" NL ").append(BaseCardRelicStats.cardToString(card));
                    }
                }

                cachedNames = b.toString();
            }

            return cachedNames;
        }
    }
}
