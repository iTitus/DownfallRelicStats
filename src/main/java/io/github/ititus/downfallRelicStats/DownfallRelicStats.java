package io.github.ititus.downfallRelicStats;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import io.github.ititus.downfallRelicStats.patches.relics.*;
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

        register(PocketSentryInfo.getInstance()); // Arumba's Pocket Sentry
        register(DefensiveModeMoreBlockInfo.getInstance()); // Baalor's Lordly Plate
        register(BarbellsInfo.getInstance()); // Barbell
        register(BlackPowderInfo.getInstance()); // Black Powder
        register(BloodyToothInfo.getInstance()); // Broken Tooth
        register(BronzeCoreInfo.getInstance()); // Bronze Core
        register(ModeShifterInfo.getInstance()); // Bronze Gear
        register(ClaspedLocketInfo.getInstance()); // Clasped Locket
        register(DecasWashersInfo.getInstance()); // Deca's Washers
        register(DefensiveTrainingManualInfo.getInstance()); // Defensive Thesis
        register(DentedPlateInfo.getInstance()); // Dented Plate
        register(GremlinBombInfo.getInstance()); // Gremlin Bomb
        register(AbsorbEndCombatInfo.getInstance()); // Heart of Goo
        register(UnbrokenSoulInfo.getInstance()); // Mark of the Ether
        register(GremlinKnobUpgradeInfo.getInstance()); // Mob Leader's Crown
        register(GremlinKnobInfo.getInstance()); // Mob Leader's Staff
        register(PricklyShieldsInfo.getInstance()); // Prickly Shields
        register(ProtectiveGogglesInfo.getInstance()); // Protective Goggles
        register(RedScarfInfo.getInstance()); // Red Scarf
        register(RyeStalkInfo.getInstance()); // Rye Stalk
        register(SixitudeInfo.getInstance()); // Six-Point Brand
        register(SneckoSoulInfo.getInstance()); // Snecko Soul
        register(SpiritBrandInfo.getInstance()); // Spirit Brand
        register(SuperSneckoSoulInfo.getInstance()); // Super Snecko Soul
        register(TagTeamworkInfo.getInstance()); // Tag Teamwork
        register(WoundPokerInfo.getInstance()); // Wound Poker
        register(BabySneckoInfo.getInstance()); // Young Snecko
    }
}
