package io.github.ititus.downfallRelicStats;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import io.github.ititus.downfallRelicStats.relics.automaton.*;
import io.github.ititus.downfallRelicStats.relics.awakened.*;
import io.github.ititus.downfallRelicStats.relics.champ.*;
import io.github.ititus.downfallRelicStats.relics.collector.*;
import io.github.ititus.downfallRelicStats.relics.gremlin.*;
import io.github.ititus.downfallRelicStats.relics.guardian.*;
import io.github.ititus.downfallRelicStats.relics.hermit.*;
import io.github.ititus.downfallRelicStats.relics.hexaghost.*;
import io.github.ititus.downfallRelicStats.relics.shared.*;
import io.github.ititus.downfallRelicStats.relics.slimebound.*;
import io.github.ititus.downfallRelicStats.relics.snecko.*;
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

        // Downfall
        register(BlackCandleInfo.getInstance());
        // Burden of Knowledge - nothing to track
        // Broken Wing Statue doesn't do anything - nothing to track
        // Coat Of Many Faces / Cloak of Many Faces gives all masks all the time - nothing to track
        // Extra Cursed Bell - same as Calling Bell, not tracked
        register(ExtraCursedKeyInfo.getInstance()); // Extra Cursed Key
        register(GremlinSackInfo.getInstance()); // Gremlin Sack
        register(GremlinWheelInfo.getInstance()); // Wheel of Change
        register(HeartBlessingBlueInfo.getInstance()); // TODO: this is deprecated, check after next update, also does not work correctly for gremlins
        register(HeartBlessingGreenInfo.getInstance()); // TODO: this is deprecated, check after next update
        register(HeartBlessingRedInfo.getInstance()); // TODO: this is deprecated, check after next update
        // The Heart's Malice - not tracked
        register(HecktoplasmInfo.getInstance()); // Hecktoplasm
        register(KnowingSkullInfo.getInstance()); // Knowing Skull
        // NeowBlessing is not registered
        // Bandit Contract (RedIOU) - not tracked
        // Bandit Contract+ (RedIOUUpgrade) - not tracked
        // Shattered Fragment- - not tracked
        register(TeleportStoneInfo.getInstance()); // Teleport Stone
        // (VFX Tester) TestRelic is not registered

        // Expansion
        // Tiny Bowler Hat (StudyCardRelic) - not tracked, always does the same

        // Automaton / Bronze
        // Analytical Core (Paradox Artifact) not tracked, does the same every turn
        register(BottledCodeInfo.getInstance()); // Bottled Code
        register(BronzeCoreInfo.getInstance()); // Bronze Core
        register(BronzeIdolInfo.getInstance()); // Bronze Idol
        register(CableSpoolInfo.getInstance()); // Cable Spool
        register(DecasWashersInfo.getInstance()); // Deca's Washers
        // Donu's Washers not tracked, does the same every combat
        // Electromagnetic Coil not tracked, static effect
        // Makeshift Battery not tracked, does the same every turn
        // Mallet not tracked, does the same every combat
        register(PlatinumCoreInfo.getInstance()); // Platinum Core
        register(ProtectiveGogglesInfo.getInstance()); // Protective Goggles
        // Silver Bullet not tracked, does the same every combat
        register(TimepieceInfo.getInstance()); // Frost Primer

        // Awakened One
        register(AbyssBladeInfo.getInstance()); // Abyss Blade
        register(AwakenedUrnInfo.getInstance()); // Awakened Urn (Bird-Faced Vase)
        register(CawingCaskInfo.getInstance()); // Cawing Cask
        register(CursedBlessingInfo.getInstance()); // Cursed Blessing (Final Rites)
        register(CurvedSwordInfo.getInstance()); // Curved Sword (Zetsumei)
        // Curved Sword Meme (Conjure Blade) not tracked, is a meme and does the same every combat
        register(DeadBirdInfo.getInstance()); // Dead Bird (Corvid Spirit)
        // Eye of the Occult not tracked, hard to quantify
        register(HexxBombInfo.getInstance()); // Hexx Bomb (Manabomb)
        register(MiniBlackHoleInfo.getInstance()); // Mini Black Hole (Alethea)
        register(MoonTalismanInfo.getInstance()); // Moon Talisman (Crescent Talisman)

        // Champ
        register(BarbellsInfo.getInstance()); // Barbell
        // Berserkers Guide To Slaughter (Berserker's Guide) not tracked, does the same every turn
        // Black Knights Helmet (Black Knight's Helm) not tracked, difficult to display and in the end it's always 1STR 1DEX
        // Champion's Crown hard to track because it uses a shared tag now
        register(ChampionCrownUpgradedInfo.getInstance()); // Champion Crown Upgraded (Victorious Crown)
        register(ChampStancesModRelicInfo.getInstance()); // New Challenger
        register(DefensiveTrainingManualInfo.getInstance()); // Defensive Thesis
        register(DeflectingBracersInfo.getInstance()); // Deflecting Bracers
        register(DuelingGloveInfo.getInstance()); // Dueling Glove
        register(FightingForDummiesInfo.getInstance()); // Dolphin's Style Guide
        register(GladiatorsBookOfMartialProwessInfo.getInstance()); // Gladiators Manual
        // Lift Relic (Inner Strength) not tracked, does the same every combat
        // Power Armor not tracked, too difficult because of patching weirdness
        register(RageAmuletInfo.getInstance()); // Amulet of Unyielding
        register(SignatureFinisherInfo.getInstance()); // Signature Finisher
        register(SpectersHandInfo.getInstance()); // Spectre's Hand

        // Collector
        register(AutoCurserInfo.getInstance()); // Hexx Talisman
        register(BagOfTricksInfo.getInstance()); // Bag of Tricks
        register(BagpipesInfo.getInstance()); // The Bagpipes
        register(BlockedChakraInfo.getInstance()); // Blocked Chakra
        register(BottledCollectibleInfo.getInstance()); // Bottled Brain
        // Emerald Torch not tracked, does the same every combat
        // Forbidden Fruit not tracked, does the same every combat
        register(FuelCanisterInfo.getInstance()); // Fuel Canister
        register(HolidayCoalInfo.getInstance()); // Krampian Coal
        register(IncenseInfo.getInstance()); // Incense // TODO: this is deprecated, check after next update
        register(JadeRingInfo.getInstance()); // Jade Ring
        register(PrismaticTorchInfo.getInstance()); // Prismatic Torch
        register(RoughDiamondInfo.getInstance()); // Rough Diamond // TODO: this is deprecated, check after next update
        // Soullit Lamp (Soul-lit Lamp) not tracked, does the same every combat
        // The Contract not tracked: one-time effect
        // Thimble Helm not tracked: too difficult because it uses a power that overrides modifyBlock which is called all the time

        // Gremlins
        register(FragmentationGrenadeInfo.getInstance()); // Fragmentation Grenade
        register(GremlinBombInfo.getInstance()); // Gremlin Bomb
        register(GremlinGravestoneInfo.getInstance()); // Gremlin Gravestone
        // Gremlin Knob (Mob Leader's Staff) not tracked, does the same every combat
        register(GremlinKnobUpgradeInfo.getInstance()); // Mob Leader's Crown
        register(ImpeccablePecsInfo.getInstance()); // Impeccable Pecs
        register(LeaderVoucherInfo.getInstance()); // Gremlin Leader's Voucher
        register(MagicMalletInfo.getInstance()); // Magic Mallet
        register(PricklyShieldsInfo.getInstance()); // Prickly Shields
        register(ShortStatureInfo.getInstance()); // Short Stature
        register(StolenMerchandiseInfo.getInstance()); // Stolen Merchandise
        register(SupplyScrollInfo.getInstance()); // Supply Scroll
        register(TagTeamworkInfo.getInstance()); // Tag Teamwork
        register(WizardHatInfo.getInstance()); // Wizard Hat
        register(WizardStaffInfo.getInstance()); // Wizard Staff
        register(WoundPokerInfo.getInstance()); // Wound Poker

        // Guardian
        register(BottledAnomalyInfo.getInstance()); // Bottled Anomaly
        register(BottledStasisInfo.getInstance()); // Bottled Black Hole
        register(DefensiveModeMoreBlockInfo.getInstance()); // Baalor's Lordly Plate
        register(GemstoneGunInfo.getInstance()); // Gemstone Gun
        register(ModeShifterInfo.getInstance()); // Bronze Gear
        register(ModeShifterPlusInfo.getInstance()); // Guardian Gear // TODO: track energy/cards instead of times triggered
        register(ObsidianScalesInfo.getInstance()); // Obsidian Scales
        register(PickAxeInfo.getInstance()); // Pick of Rhapsody
        register(PocketSentryInfo.getInstance()); // Arumba's Pocket Sentry
        // TODO: register(SackOfGemsInfo.getInstance()); // Sack of Gems
        register(StasisCodexInfo.getInstance()); // Pilot's Codex
        register(StasisEggInfo.getInstance()); // Quantum Chamber
        // Stasis Slot Reduction (Wander Bots) not tracked: constant effect (energy relic)
        register(StasisUpgradeRelicInfo.getInstance()); // Cryo Chamber

        // Hermit
        register(BartenderGlassInfo.getInstance()); // Shotglass
        register(BlackPowderInfo.getInstance()); // Black Powder
        register(BloodyToothInfo.getInstance()); // Broken Tooth
        register(BrassTacksInfo.getInstance()); // Brass Tacks
        register(CharredGloveInfo.getInstance()); // Charred Glove
        register(ClaspedLocketInfo.getInstance()); // Clasped Locket
        register(DentedPlateInfo.getInstance()); // Dented Plate
        register(HorseshoeInfo.getInstance()); // Horseshoe
        // Memento (Old Locket) not tracked, does the same every combat
        register(PetGhostInfo.getInstance()); // Pet Ghost
        register(RedScarfInfo.getInstance()); // Red Scarf
        register(RyeStalkInfo.getInstance()); // Rye Stalk
        register(SpyglassInfo.getInstance()); // Spyglass
        register(StraightRazorInfo.getInstance()); // Straight Razor

        // Hexaghost
        register(BolsterEngineInfo.getInstance()); // Tricky's Bolster-Rod
        // Red Candle (Candle of Cauterizing) not tracked, too many places to hook
        register(IceCubeInfo.getInstance()); // Xanatos' Icy Charm
        // Jar of TOBSCo (Inflammatory Letter) not tracked: same effect every combat
        register(JarOfFuelInfo.getInstance()); // Olexa's Shield
        register(LibraInfo.getInstance()); // Libra
        register(MatchstickCaseInfo.getInstance()); // Sneaky Teakwood Match
        register(RecyclingMachineInfo.getInstance()); // Recycler
        register(SixitudeInfo.getInstance()); // Six-Point Brand
        register(SoulConsumerInfo.getInstance()); // Soul Stone (old: Thermal Stone)
        // Soul of Chaos not tracked, does the same every turn // TODO: maybe track randomized ghostflames?
        register(SpiritBrandInfo.getInstance()); // Spirit Brand
        register(TheBrokenSealInfo.getInstance()); // The Broken Seal // TODO: this is deprecated, check after next update
        register(UnbrokenSoulInfo.getInstance()); // Mark of the Ether

        // Slime Boss
        register(AbsorbEndCombatInfo.getInstance()); // Heart of Goo
        register(AbsorbEndCombatUpgradedInfo.getInstance()); // Black Heart of Goo
        // Aggressive Slime Relic (Goop Dweller) not tracked, does the same every combat
        register(GreedOozeRelicInfo.getInstance()); // Greed Ooze
        // Max Slimes Relic (Jeremiah's Banner) not tracked, does the same every combat
        // Potency Relic (Stone of Nomakk) not tracked, does the same every combat
        register(PreparedRelicInfo.getInstance()); // Slime Soup
        register(ScrapOozeRelicInfo.getInstance()); // Scrap Ooze
        register(SelfDamagePreventRelicInfo.getInstance()); // Protective Gear
        // Slimed Skull Relic (Slimy Skull) not tracked, too difficult because it uses a power
        register(SlimedTailRelicInfo.getInstance()); // Slimed Tail
        register(StickyStickInfo.getInstance()); // Gelatinous Cube
        // Tar Blob (Tarr Blob) not tracked, does the same every combat

        // Snecko
        register(BabySneckoInfo.getInstance()); // Young Snecko
        // Blank Card not tracked, does the same every combat
        register(CleanMudInfo.getInstance()); // Snake-Charmer's Flute
        register(ConfusingCodexInfo.getInstance()); // Ring of the Snek
        register(CrystallizedMudInfo.getInstance()); // Snake-Charmer's Flute
        register(D8Info.getInstance()); // Mystical Octahedron
        register(LoadedDieInfo.getInstance()); // Loaded Die
        // TODO: Rare Booster Pack (Rare Booster Box) - find a way to track card reward
        // Sleeved Ace not tracked, does the same every combat
        // TODO: Snecko Boss (Lucky Horseshoe) - find a way to track card reward
        // TODO: Snecko Common (Seal of Approval) - find a way to track card reward
        // Snecko Soul not tracked, does the same every combat
        // Snecko Talon (Idol of Retromation) not tracked, does the same every turn
        // Super Snecko Eye not tracked, does the same every turn
        // Super Snecko Soul not tracked, does the same every other turn
        // Unknown Egg (Unidentified Egg) not tracked, too difficult
    }

    private static void unlockAll() {
        LOGGER.info("locked cards: {}", UnlockTracker.lockedCards);
        for (String cardId : CardLibrary.cards.keySet()) {
            // LOGGER.info("unlock card: {}", cardId);
            UnlockTracker.unlockCard(cardId);
        }
        LOGGER.info("locked cards after: {}", UnlockTracker.lockedCards);

        LOGGER.info("locked relics: {}", UnlockTracker.lockedRelics);
        for (String relicId : BaseMod.listAllRelicIDs()) {
            // LOGGER.info("unlock relic: {}", relicId);
            UnlockTracker.unlockPref.putInteger(relicId, 2);
            UnlockTracker.lockedRelics.remove(relicId);
            UnlockTracker.markRelicAsSeen(relicId);
        }
        LOGGER.info("locked relics after: {}", UnlockTracker.lockedRelics);

        LOGGER.info("locked characters: {}", UnlockTracker.lockedCharacters);
        for (String character : UnlockTracker.lockedCharacters.toArray(new String[0])) {
            UnlockTracker.unlockPref.putInteger(character, 2);
            UnlockTracker.lockedCharacters.remove(character);
        }
        LOGGER.info("locked characters after: {}", UnlockTracker.lockedCharacters);

        LOGGER.info("locked loadouts: {}", UnlockTracker.lockedLoadouts);
        UnlockTracker.lockedLoadouts.clear();

        for (AbstractPlayer p : CardCrawlGame.characterManager.getAllCharacters()) {
            // LOGGER.info("unlock character: {}", p.chosenClass);
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
