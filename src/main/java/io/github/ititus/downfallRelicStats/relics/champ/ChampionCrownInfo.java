package io.github.ititus.downfallRelicStats.relics.champ;

import champ.cards.StanceDanceCrown;
import champ.relics.ChampionCrown;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;

import java.text.DecimalFormat;

public final class ChampionCrownInfo extends BaseRelicStats<ChampionCrownInfo.Stats> {

    private static final ChampionCrownInfo INSTANCE = new ChampionCrownInfo();

    private ChampionCrownInfo() {
        super(ChampionCrown.ID, Stats.class);
    }

    public static ChampionCrownInfo getInstance() {
        return INSTANCE;
    }

    public static class Stats implements StatContainer {

        int defensive = 0;
        int berserker = 0;

        @Override
        public String getDescription(String[] description) {
            return description[0] + defensive +
                    description[1] + berserker;
        }

        @Override
        public String getExtendedDescription(String[] description, String[] extendedDescription, int totalTurns, int totalCombats) {
            DecimalFormat df = new DecimalFormat("#%");
            int total = Math.max(1, defensive + berserker);
            return description[2] + df.format((double) defensive / total) +
                    description[3] + df.format((double) berserker / total);
        }
    }

    @SpirePatch(
            clz = StanceDanceCrown.class,
            method = "doChoiceStuff"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix(StanceDanceCrown __instance, AbstractMonster m, AbstractCard card) {
            switch (card.cardID) {
                case "octo:OctoBerserk":
                    getInstance().stats.berserker++;
                    break;
                case "octo:OctoDefense":
                    getInstance().stats.defensive++;
                    break;
            }
        }
    }
}
