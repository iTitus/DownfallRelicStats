package io.github.ititus.downfallRelicStats;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.localization.UIStrings;
import io.github.ititus.downfallRelicStats.patches.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.RelicStats;

@SpireInitializer
@SuppressWarnings("unused")
public final class DownfallRelicStats implements EditStringsSubscriber, PostInitializeSubscriber, OnPlayerLoseBlockSubscriber {

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
        BaseMod.loadCustomStringsFile(UIStrings.class, makePath("localization/eng/descriptions.json"));
        /*switch (Settings.language) {
            case DEU:
                BaseMod.loadCustomStringsFile(UIStrings.class, makePath("localization/deu/descriptions.json"));
        }*/
    }

    @Override
    public void receivePostInitialize() {
        LOGGER.info("POST INIT");

        register(PocketSentryInfo.getInstance()); // Arumba's Pocket Sentry
        register(DefensiveModeMoreBlockInfo.getInstance()); // Baalor's Lordly Plate
        register(BarbellsInfo.getInstance()); // Barbell
        register(AbsorbEndCombatUpgradedInfo.getInstance()); // Black Heart of Goo
        register(BlackPowderInfo.getInstance()); // Black Powder
        register(BottledAnomalyInfo.getInstance()); // Bottled Anomaly
        register(BottledStasisInfo.getInstance()); // Bottled Black Hole
        register(BottledCodeInfo.getInstance()); // Bottled Code
        register(BrassTacksInfo.getInstance()); // Brass Tacks
        register(BloodyToothInfo.getInstance()); // Broken Tooth
        register(BronzeCoreInfo.getInstance()); // Bronze Core
        register(ModeShifterInfo.getInstance()); // Bronze Gear
        register(CableSpoolInfo.getInstance()); // Cable Spool
        register(ChampionCrownInfo.getInstance()); // Champion's Crown
        register(CharredGloveInfo.getInstance()); // Charred Glove
        register(ClaspedLocketInfo.getInstance()); // Clasped Locket
        register(StasisUpgradeRelicInfo.getInstance()); // Cryo Chamber
        register(DecasWashersInfo.getInstance()); // Deca's Washers
        register(DefensiveTrainingManualInfo.getInstance()); // Defensive Thesis
        register(DeflectingBracersInfo.getInstance()); // Deflecting Bracers
        register(DentedPlateInfo.getInstance()); // Dented Plate
        register(DuelingGloveInfo.getInstance()); // Dueling Glove
        register(ExtraCursedKeyInfo.getInstance()); // Extra Cursed Key
        register(GemstoneGunInfo.getInstance()); // Gemstone Gun
        register(GladiatorsBookOfMartialProwessInfo.getInstance()); // Gladiators Manual
        register(GreedOozeRelicInfo.getInstance()); // Greed Ooze
        register(GremlinBombInfo.getInstance()); // Gremlin Bomb
        register(GremlinGravestoneInfo.getInstance()); // Gremlin Gravestone
        register(LeaderVoucherInfo.getInstance()); // Gremlin Leader's Voucher
        register(GremlinSackInfo.getInstance()); // Gremlin Sack
        register(AbsorbEndCombatInfo.getInstance()); // Heart of Goo
        register(HorseshoeInfo.getInstance()); // Horseshoe
        register(ImpeccablePecsInfo.getInstance()); // Impeccable Pecs
        register(MagicMalletInfo.getInstance()); // Magic Mallet
        register(UnbrokenSoulInfo.getInstance()); // Mark of the Ether
        register(GremlinKnobUpgradeInfo.getInstance()); // Mob Leader's Crown
        register(GremlinKnobInfo.getInstance()); // Mob Leader's Staff
        register(D8Info.getInstance()); // Mystical Octahedron
        register(ChampStancesModRelicInfo.getInstance()); // New Challenger
        register(PetGhostInfo.getInstance()); // Pet Ghost
        register(PickAxeInfo.getInstance()); // Pick of Rhapsody
        register(StasisCodexInfo.getInstance()); // Pilot's Codex
        register(PlatinumCoreInfo.getInstance()); // Platinum Core
        register(PricklyShieldsInfo.getInstance()); // Prickly Shields
        register(ProtectiveGogglesInfo.getInstance()); // Protective Goggles
        register(StasisEggInfo.getInstance()); // Quantum Chamber
        register(CandleOfCauterizingInfo.getInstance()); // Red Candle
        register(RedScarfInfo.getInstance()); // Red Scarf
        register(ConfusingCodexInfo.getInstance()); // Ring of the Snek
        register(RyeStalkInfo.getInstance()); // Rye Stalk
        register(SackOfGemsInfo.getInstance()); // Sack of Gems
        register(ScrapOozeRelicInfo.getInstance()); // Scrap Ooze
        register(SignatureFinisherInfo.getInstance()); // Six-Point Brand
        register(SixitudeInfo.getInstance()); // Six-Point Brand
        register(PreparedRelicInfo.getInstance()); // Slime Soup
        register(SneckoSoulInfo.getInstance()); // Snecko Soul
        register(SpiritBrandInfo.getInstance()); // Spirit Brand
        register(StolenMerchandiseInfo.getInstance()); // Stolen Merchandise
        register(StraightRazorInfo.getInstance()); // Straight Razor
        register(SuperSneckoSoulInfo.getInstance()); // Super Snecko Soul
        register(TagTeamworkInfo.getInstance()); // Tag Teamwork
        register(TheBrokenSealInfo.getInstance()); // The Broken Seal
        register(SoulConsumerInfo.getInstance()); // Thermal Stone
        register(BolsterEngineInfo.getInstance()); // Tricky's Bolster-Rod
        register(UnknownEggInfo.getInstance()); // Unidentified Egg
        register(WizardHatInfo.getInstance()); // Wizard Hat
        register(WoundPokerInfo.getInstance()); // Wound Poker
        register(BabySneckoInfo.getInstance()); // Young Snecko
    }

    @Override
    public int receiveOnPlayerLoseBlock(int blockToExpire) {
        DeflectingBracersInfo.getInstance().trigger(blockToExpire);
        return blockToExpire;
    }
}
