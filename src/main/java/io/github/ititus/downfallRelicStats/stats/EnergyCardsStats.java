package io.github.ititus.downfallRelicStats.stats;

import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;
import relicstats.AmountAdjustmentCallback;

public class EnergyCardsStats implements StatContainer {

    public int energy = 0;
    public int cards = 0;

    @Override
    public String getDescription(String[] description) {
        return description[0] + energy +
                description[1] + cards;
    }

    @Override
    public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
        return BaseCombatRelicStats.generateExtendedDescription(description, 2, energy, totalTurns, totalCombats) +
                BaseCombatRelicStats.generateExtendedDescription(description, 4, cards, totalTurns, totalCombats);
    }

    public class CardsAdjuster implements AmountAdjustmentCallback {

        private int startingAmount;

        @Override
        public void registerStartingAmount(int startingAmount) {
            this.startingAmount = startingAmount;
        }

        @Override
        public void registerEndingAmount(int endingAmount) {
            EnergyCardsStats.this.cards += endingAmount - this.startingAmount;
        }
    }
}
