package io.github.ititus.downfallRelicStats;

public interface StatContainer {

    String getDescription(String[] description);

    default String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
        return "";
    }
}
