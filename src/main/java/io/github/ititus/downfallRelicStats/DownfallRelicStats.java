package io.github.ititus.downfallRelicStats;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import io.github.ititus.downfallRelicStats.patches.relics.BlackPowderInfo;
import io.github.ititus.downfallRelicStats.patches.relics.GremlinBombInfo;
import io.github.ititus.downfallRelicStats.patches.relics.PricklyShieldsInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.RelicStats;

@SpireInitializer
@SuppressWarnings("unused")
public final class DownfallRelicStats implements EditStringsSubscriber, PostInitializeSubscriber {

    public static final String MOD_ID = "downfallRelicStats";
    private static final Logger LOGGER = LogManager.getLogger(DownfallRelicStats.class.getName());

    public DownfallRelicStats() {
        LOGGER.info("CONSTRUCT");
    }

    public static String makeId(String idText) {
        return MOD_ID + ":" + idText;
    }

    public static String makePath(String resourcePath) {
        return MOD_ID + "Resources/" + resourcePath;
    }

    public static void initialize() {
        BaseMod.subscribe(new DownfallRelicStats());
    }

    private static void register(BaseRelicStats<?> relicStats) {
        RelicStats.registerCustomStats(relicStats.getRelicId(), relicStats);
    }

    @Override
    public void receiveEditStrings() {
        switch (Settings.language) {
            default:
                BaseMod.loadCustomStringsFile(UIStrings.class, makePath("localization/eng/descriptions.json"));
        }
    }

    @Override
    public void receivePostInitialize() {
        LOGGER.info("POST INIT");

        register(BlackPowderInfo.getInstance());
        register(GremlinBombInfo.getInstance());
        register(PricklyShieldsInfo.getInstance());
    }
}
