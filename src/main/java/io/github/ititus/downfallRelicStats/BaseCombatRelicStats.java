package io.github.ititus.downfallRelicStats;

import com.google.gson.JsonElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.AmountAdjustmentCallback;
import relicstats.AmountIncreaseCallback;

import java.text.DecimalFormat;

public abstract class BaseCombatRelicStats extends BaseRelicStats<BaseCombatRelicStats.Stats> implements AmountIncreaseCallback, AmountAdjustmentCallback {

    private static final Logger LOGGER = LogManager.getLogger(BaseCombatRelicStats.class.getName());

    protected boolean showPerTurn = true;
    protected boolean showPerCombat = true;
    protected boolean amountAdjustInvert = false;

    private int startingAmount;

    protected BaseCombatRelicStats(String relicId) {
        super(relicId, Stats.class);
    }

    public static String generateExtendedDescription(String[] description, int descriptionStart, int amount, int totalTurns, int totalCombats) {
        DecimalFormat df = new DecimalFormat("#.###");
        return (totalTurns > 0 ? (description[descriptionStart] + df.format((double) amount / totalTurns)) : "") +
                (totalCombats > 0 ? (description[descriptionStart + 1] + df.format((double) amount / totalCombats)) : "");
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription() + stats.getExtendedDescription(getDescription(), getExtendedDescription(), showPerTurn ? Math.max(1, totalTurns) : 0, showPerCombat ? Math.max(1, totalCombats) : 0);
    }

    @Override
    public void increaseAmount(int by) {
        stats.increaseAmount(by);
    }

    @Override
    public void registerStartingAmount(int startingAmount) {
        this.startingAmount = startingAmount;
    }

    @Override
    public void registerEndingAmount(int endingAmount) {
        increaseAmount(amountAdjustInvert ? startingAmount - endingAmount : endingAmount - startingAmount);
    }

    @Override
    public void onLoadRaw(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonArray()) {
            try {
                stats.amount = jsonElement.getAsJsonArray().get(0).getAsInt();
                LOGGER.debug("Old serialization encountered in {}", getClass());
                return;
            } catch (Exception e) {
                LOGGER.warn("Error while loading old serialization in {}", getClass(), e);
            }
        }

        super.onLoadRaw(jsonElement);
    }

    public static class Stats implements StatContainer, AmountIncreaseCallback {

        int amount = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + amount;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            return generateExtendedDescription(extendedDescription, 0, amount, totalTurns, totalCombats);
        }

        @Override
        public void increaseAmount(int by) {
            amount += by;
        }
    }
}
