package io.github.ititus.downfallRelicStats.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import downfall.patches.TeleportStonePatch;
import downfall.relics.TeleportStone;
import io.github.ititus.downfallRelicStats.BaseRelicStats;
import io.github.ititus.downfallRelicStats.StatContainer;

public class TeleportStoneInfo extends BaseRelicStats<TeleportStoneInfo.Stats> {
    private static final TeleportStoneInfo INSTANCE = new TeleportStoneInfo();

    private TeleportStoneInfo() {
        super(TeleportStone.ID, Stats.class);
    }

    public static TeleportStoneInfo getInstance() {
        return INSTANCE;
    }
    
    public static class Stats implements StatContainer {
        int skips = 0;
        public String getDescription(String[] description) {
            return description[0] + skips;
        }
    }

    @SpirePatch(clz = TeleportStonePatch.NodeSelected.class,method = "Postfix")
    public static class SkipPatch {
        public static void Postfix(MapRoomNode __instance) {
            if (AbstractDungeon.player.hasRelic(INSTANCE.getRelicId()))
                getInstance().stats.skips += (int)ReflectionHacks.privateStaticMethod(TeleportStonePatch.IsConnectedTo.class, "getNodeDistance", new Class[] {MapRoomNode.class, MapRoomNode.class}).invoke(new Object[] {AbstractDungeon.currMapNode, __instance}) - 1;
        }
    }
}