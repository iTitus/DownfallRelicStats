package io.github.ititus.downfallRelicStats;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import io.github.ititus.downfallRelicStats.patches.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relicstats.RelicStats;

import java.util.Map;

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
        if (relicStats instanceof ISubscriber) {
            BaseMod.subscribe((ISubscriber) relicStats);
        }
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

        // unlockAll();

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
        register(BronzeIdolInfo.getInstance()); // Bronze Idol
        register(CableSpoolInfo.getInstance()); // Cable Spool
        register(ChampionCrownInfo.getInstance()); // Champion's Crown
        register(CharredGloveInfo.getInstance()); // Charred Glove
        register(ClaspedLocketInfo.getInstance()); // Clasped Locket
        register(StasisUpgradeRelicInfo.getInstance()); // Cryo Chamber
        register(DecasWashersInfo.getInstance()); // Deca's Washers
        register(DefensiveTrainingManualInfo.getInstance()); // Defensive Thesis
        register(DeflectingBracersInfo.getInstance()); // Deflecting Bracers
        register(DentedPlateInfo.getInstance()); // Dented Plate
        register(FightingForDummiesInfo.getInstance()); // Dolphin's Style Guide
        register(DuelingGloveInfo.getInstance()); // Dueling Glove
        register(ExtraCursedKeyInfo.getInstance()); // Extra Cursed Key
        register(FragmentationGrenadeInfo.getInstance()); // Fragmentation Grenade
        register(TimepieceInfo.getInstance()); // Frost Primer
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
        register(KnowingSkullInfo.getInstance()); // Knowing Skull
        register(SneckoBossInfo.getInstance()); // Lucky Horseshoe
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
        register(SelfDamagePreventRelicInfo.getInstance()); // Protective Gear
        register(ProtectiveGogglesInfo.getInstance()); // Protective Goggles
        register(StasisEggInfo.getInstance()); // Quantum Chamber
        register(RecyclingMachineInfo.getInstance()); // Recycler
        register(CandleOfCauterizingInfo.getInstance()); // Red Candle
        register(RedScarfInfo.getInstance()); // Red Scarf
        register(ConfusingCodexInfo.getInstance()); // Ring of the Snek
        register(RyeStalkInfo.getInstance()); // Rye Stalk
        register(SackOfGemsInfo.getInstance()); // Sack of Gems
        register(ScrapOozeRelicInfo.getInstance()); // Scrap Ooze
        register(SneckoCommonInfo.getInstance()); // Seal of Approval
        register(ShortStatureInfo.getInstance()); // Short Stature
        register(BartenderGlassInfo.getInstance()); // Shotglass
        register(SignatureFinisherInfo.getInstance()); // Signature Finisher
        register(SixitudeInfo.getInstance()); // Six-Point Brand
        register(PreparedRelicInfo.getInstance()); // Slime Soup
        register(SlimedTailRelicInfo.getInstance()); // Slimed Tail
        register(CleanMudInfo.getInstance()); // Snake-Charmer's Flute
        register(SneckoSoulInfo.getInstance()); // Snecko Soul
        register(SoulOfChaosInfo.getInstance()); // Soul of Chaos
        register(SpectersHandInfo.getInstance()); // Spectre's Hand
        register(SpiritBrandInfo.getInstance()); // Spirit Brand
        register(SpyglassInfo.getInstance()); // Spyglass
        register(StolenMerchandiseInfo.getInstance()); // Stolen Merchandise
        register(StraightRazorInfo.getInstance()); // Straight Razor
        register(SuperSneckoEyeInfo.getInstance()); // Super Snecko Eye
        register(SuperSneckoSoulInfo.getInstance()); // Super Snecko Soul
        register(SupplyScrollInfo.getInstance()); // Supply Scroll
        register(TagTeamworkInfo.getInstance()); // Tag Teamwork
        register(TeleportStoneInfo.getInstance()); // Teleport Stone
        register(TheBrokenSealInfo.getInstance()); // The Broken Seal
        register(SoulConsumerInfo.getInstance()); // Thermal Stone
        register(BolsterEngineInfo.getInstance()); // Tricky's Bolster-Rod
        register(UnknownEggInfo.getInstance()); // Unidentified Egg
        register(GremlinWheelInfo.getInstance()); // Wheel of Change
        register(WizardHatInfo.getInstance()); // Wizard Hat
        register(WizardStaffInfo.getInstance()); // Wizard Staff
        register(WoundPokerInfo.getInstance()); // Wound Poker
        register(BabySneckoInfo.getInstance()); // Young Snecko

        // Collector
        register(AutoCurserInfo.getInstance()); // Hexx Talisman
        register(BagOfTricksInfo.getInstance()); // Bag of Tricks
        register(BagpipesInfo.getInstance()); // The Bagpipes
        register(BlockedChakraInfo.getInstance()); // Blocked Chakra
        register(BottledCollectibleInfo.getInstance()); // Bottled Brain
        register(EmeraldTorchInfo.getInstance()); // Emerald Torch
        register(ForbiddenFruitInfo.getInstance()); // Forbidden Fruit
        register(FuelCanisterInfo.getInstance()); // Fuel Canister
        register(HolidayCoalInfo.getInstance()); // Krampian Coal
        register(IncenseInfo.getInstance()); // Incense
        register(JadeRingInfo.getInstance()); // Jade Ring
        register(PrismaticTorchInfo.getInstance()); // Prismatic Torch
        register(RoughDiamondInfo.getInstance()); // Rough Diamond
        register(SoullitLampInfo.getInstance()); // Soul-lit Lamp
        // The Contract not tracked: one-time effect
        // Thimble Helm not tracked: too difficult because it uses a power that overrides modifyBlock which is called all the time
    }

    private static void unlockAll() {
        LOGGER.info("locked cards: {}", UnlockTracker.lockedCards);
        for (Map.Entry<String, AbstractCard> cardEntry : CardLibrary.cards.entrySet()) {
            String cardId = cardEntry.getKey();
            AbstractCard card = cardEntry.getValue();

            // LOGGER.info("unlock card: {}", cardId);
            UnlockTracker.unlockPref.putInteger(cardId, 2);
            UnlockTracker.seenPref.putInteger(cardId, 1);

            if (card != null && !card.isSeen) {
                card.isSeen = true;
                card.unlock();
            }
        }
        UnlockTracker.lockedCards.clear();

        LOGGER.info("locked relics: {}", UnlockTracker.lockedRelics);
        for (String relicId : BaseMod.listAllRelicIDs()) {
            // LOGGER.info("unlock relic: {}", relicId);
            UnlockTracker.unlockPref.putInteger(relicId, 2);
            UnlockTracker.relicSeenPref.putInteger(relicId, 1);

            AbstractRelic relic = RelicLibrary.getRelic(relicId);
            if (relic != null) {
                relic.isSeen = true;
            }
        }
        UnlockTracker.lockedRelics.clear();

        LOGGER.info("locked characters: {}", UnlockTracker.lockedCharacters);
        for (String character : UnlockTracker.lockedCharacters) {
            UnlockTracker.unlockPref.putInteger(character, 2);
        }
        UnlockTracker.lockedCharacters.clear();

        LOGGER.info("locked loadouts: {}", UnlockTracker.lockedLoadouts);
        UnlockTracker.lockedLoadouts.clear();

        for (AbstractPlayer p : CardCrawlGame.characterManager.getAllCharacters()) {
            int maxUnlockLevel = BaseMod.isBaseGameCharacter(p) ? 5 : BaseMod.getMaxUnlockLevel(p);
            int unlockLevel = UnlockTracker.getUnlockLevel(p.chosenClass);
            UnlockTracker.unlockProgress.putInteger(p.chosenClass + "UnlockLevel", Math.max(unlockLevel, maxUnlockLevel + 1));

            Prefs prefs = p.getPrefs();
            prefs.putInteger("WIN_COUNT", Math.max(1, prefs.getInteger("WIN_COUNT", 0)));
            prefs.putInteger("ASCENSION_LEVEL", 20);
            prefs.flush();

            CardCrawlGame.playerPref.putBoolean(p.chosenClass + "_WIN", true);

            LOGGER.info("playerClass={} unlockLevel={} maxUnlockLevel={}", p.chosenClass, unlockLevel, maxUnlockLevel);
        }

        UnlockTracker.unlockPref.flush();
        UnlockTracker.seenPref.flush();
        UnlockTracker.relicSeenPref.flush();
        UnlockTracker.unlockProgress.flush();
        CardCrawlGame.playerPref.flush();
    }
}
