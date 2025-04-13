package io.github.ititus.downfallRelicStats.stats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import io.github.ititus.downfallRelicStats.StatContainer;

import java.util.Objects;

public class CardColorStats implements StatContainer {

    public String cardColor;

    @Override
    public String getDescription(String[] description) {
        if (cardColor == null) {
            return description[0] + description[1];
        }

        AbstractCard.CardColor colorEnumValue = null;
        try {
            colorEnumValue = AbstractCard.CardColor.valueOf(cardColor);
        } catch (IllegalArgumentException ignored) {
        }

        String className = null;
        if (colorEnumValue != null) {
            for (AbstractPlayer character : CardCrawlGame.characterManager.getAllCharacters()) {
                if (character != null && colorEnumValue.equals(character.getCardColor())) {
                    className = character.getLocalizedCharacterName();
                    break;
                }
            }
        }

        if (className != null) {
            return description[0] + className;
        } else {
            return description[0] + cardColor;
        }
    }

    public void setColor(AbstractCard.CardColor cardColor) {
        this.cardColor = Objects.requireNonNull(cardColor, "cardColor").name();
    }
}
