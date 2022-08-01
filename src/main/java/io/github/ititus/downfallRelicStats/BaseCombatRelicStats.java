package io.github.ititus.downfallRelicStats;

import com.google.gson.JsonElement;
import io.github.ititus.downfallRelicStats.patches.relics.BlackPowderInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.AmountAdjustmentCallback;
import relicstats.AmountIncreaseCallback;

import java.text.DecimalFormat;

public abstract class BaseCombatRelicStats extends BaseRelicStats<BaseCombatRelicStats.Stats> implements AmountIncreaseCallback, AmountAdjustmentCallback {

    private static final Logger LOGGER = LogManager.getLogger(BlackPowderInfo.class.getName());
    private int startingAmount;

    protected BaseCombatRelicStats(String relicId) {
        super(relicId, Stats.class);
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
        increaseAmount(endingAmount - startingAmount);
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

    static class Stats implements StatContainer, AmountIncreaseCallback {

        int amount = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + amount;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            DecimalFormat df = new DecimalFormat("#.###");
            return extendedDescription[0] + df.format((double) amount / totalTurns) +
                    extendedDescription[1] + df.format((double) amount / totalCombats);
        }

        @Override
        public void increaseAmount(int by) {
            amount += by;
        }
    }
}
