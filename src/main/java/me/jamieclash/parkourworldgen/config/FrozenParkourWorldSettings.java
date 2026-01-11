package me.jamieclash.parkourworldgen.config;

public record FrozenParkourWorldSettings(boolean enableOverworld, boolean enableNether,
                                         boolean enableEnd, int overworldGap, int netherGap, int endGap){}
