package com.edgerunners.cyberpunk.util;

public interface ICyberwareData {
    float getCyberpsychosisLevel();
    void setCyberpsychosisLevel(float level);
    void addCyberpsychosis(float amount);

    int getSandevistanTicks();
    void setSandevistanTicks(int ticks);
}
