package io.github.ititus.downfallRelicStats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.StatsInfo;

import java.util.Objects;

public abstract class BaseRelicStats<T extends StatContainer> extends StatsInfo {

    protected static final Gson GSON = new GsonBuilder().create();
    private static final Logger LOGGER = LogManager.getLogger(BaseRelicStats.class.getName());
    protected final String relicId;
    protected final Class<T> statsClass;
    protected T stats;
    private String[] description;
    private String[] extendedDescription;

    protected BaseRelicStats(String relicId, Class<T> statsClass) {
        this.relicId = Objects.requireNonNull(relicId, "relicId");
        this.statsClass = Objects.requireNonNull(statsClass, "statsClass");
        resetStats();
    }

    public String getRelicId() {
        return relicId;
    }

    public T getStats() {
        return stats;
    }

    public String[] getDescription() {
        if (description == null) {
            description = Objects.requireNonNull(CardCrawlGame.languagePack.getUIString(getLocId(relicId)).TEXT, "description");
        }

        return description;
    }

    public String[] getExtendedDescription() {
        if (extendedDescription == null) {
            extendedDescription = Objects.requireNonNull(CardCrawlGame.languagePack.getUIString(getLocId("EXTENDED")).TEXT, "extendedDescription");
        }

        return extendedDescription;
    }

    @Override
    public String getStatsDescription() {
        return stats.getDescription(getDescription());
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription() + stats.getExtendedDescription(getDescription(), getExtendedDescription(), Math.max(1, totalTurns), Math.max(1, totalCombats));
    }

    @Override
    public void resetStats() {
        try {
            stats = statsClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement onSaveRaw() {
        return GSON.toJsonTree(stats);
    }

    @Override
    public void onLoadRaw(JsonElement jsonElement) {
        if (jsonElement != null) {
            try {
                stats = GSON.fromJson(jsonElement, statsClass);
            } catch (JsonSyntaxException e) {
                LOGGER.warn("Error while loading new serialization in {}", getClass(), e);
                resetStats();
            }
        } else {
            resetStats();
        }
    }
}
