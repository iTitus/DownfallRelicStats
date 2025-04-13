package io.github.ititus.downfallRelicStats.stats;

import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public class EnergyCardsStatsOnlyTurns extends EnergyCardsStats {

    @Override
    public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
        return BaseCombatRelicStats.generateExtendedDescription(description, 2, energy, totalTurns, 0) +
                BaseCombatRelicStats.generateExtendedDescription(description, 3, cards, totalTurns, 0);
    }
}
