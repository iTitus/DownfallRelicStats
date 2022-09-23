package io.github.ititus.downfallRelicStats.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import downfall.patches.TeleportStonePatch;
import downfall.relics.TeleportStone;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public class TeleportStoneInfo extends BaseCombatRelicStats {

    private static final TeleportStoneInfo INSTANCE = new TeleportStoneInfo();

    private TeleportStoneInfo() {
        super(TeleportStone.ID);
        this.showPerTurn = false;
        this.showPerCombat = false;
    }

    public static TeleportStoneInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = TeleportStonePatch.NodeSelected.class,
            method = "Postfix"
    )
    @SuppressWarnings("unused")
    public static class Patch {
        public static void Postfix(MapRoomNode __instance) {
            if (AbstractDungeon.player.hasRelic(TeleportStone.ID)) {
                ReflectionHacks.RStaticMethod getNodeDistance = ReflectionHacks.privateStaticMethod(TeleportStonePatch.IsConnectedTo.class, "getNodeDistance", MapRoomNode.class, MapRoomNode.class);
                getInstance().stats.increaseAmount(Math.max(0, (int) getNodeDistance.invoke(new Object[] { AbstractDungeon.currMapNode, __instance }) - 1));
            }
        }
    }
}
