package io.github.ititus.downfallRelicStats.actions;

@FunctionalInterface
public interface PowerChangeCallback {

    void onPowerChanged(String powerId, int amount);

}
