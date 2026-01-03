package io.github.ititus.downfallRelicStats.patches.track;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import io.github.ititus.downfallRelicStats.DownfallRelicStats;

import java.util.List;

@AbstractCardModifier.SaveIgnore // do not save this modifier
public final class SourceModifier extends AbstractCardModifier {

    public static final String ID = DownfallRelicStats.makeId("SourceModifier");

    private int act = -1;
    private int floor = -1;
    private RewardItem rewardItem;

    public static SourceModifier create(RewardItem rewardItem) {
        SourceModifier m = new SourceModifier();
        if (CardCrawlGame.isInARun()) {
            m.act = AbstractDungeon.actNum;
            m.floor = AbstractDungeon.floorNum;
            m.rewardItem = rewardItem;
        }
        return m;
    }

    private static SourceModifier applyTo(AbstractCard card, RewardItem rewardItem) {
        if (card != null) {
            SourceModifier mod = SourceModifier.create(rewardItem);
            CardModifierManager.addModifier(card, mod);
            return mod;
        } else {
            return null;
        }
    }

    private static void applyTo(Iterable<? extends AbstractCard> cards, RewardItem rewardItem) {
        if (cards != null) {
            for (AbstractCard card : cards) {
                if (card != null) {
                    applyTo(card, rewardItem);
                }
            }
        }
    }

    public static void applyTo(RewardItem rewardItem) {
        if (rewardItem != null) {
            applyTo(rewardItem.cards, rewardItem);
        }
    }

    public static SourceModifier get(AbstractCard card) {
        if (card != null) {
            List<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
            if (!modifiers.isEmpty()) {
                return (SourceModifier) modifiers.get(0);
            }
        }
        return null;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        SourceModifier m = new SourceModifier();
        m.act = this.act;
        m.floor = this.floor;
        m.rewardItem = this.rewardItem;
        return m;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    public int getAct() {
        return this.act;
    }

    public int getFloor() {
        return this.floor;
    }

    public RewardItem getRewardItem() {
        return this.rewardItem;
    }
}
