package io.github.ititus.downfallRelicStats.relics.gremlin;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import gremlin.relics.StolenMerchandise;
import io.github.ititus.downfallRelicStats.BaseCombatRelicStats;

public final class StolenMerchandiseInfo extends BaseCombatRelicStats {

    private static final StolenMerchandiseInfo INSTANCE = new StolenMerchandiseInfo();

    private StolenMerchandiseInfo() {
        super(ReflectionHacks.getPrivateStatic(StolenMerchandise.class, "ID"));
    }

    public static StolenMerchandiseInfo getInstance() {
        return INSTANCE;
    }

    @SpirePatch(
            clz = StolenMerchandise.class,
            method = "onGremlinSwap"
    )
    @SuppressWarnings("unused")
    public static class Patch {

        public static void Postfix() {
            getInstance().trigger();
        }
    }
}
