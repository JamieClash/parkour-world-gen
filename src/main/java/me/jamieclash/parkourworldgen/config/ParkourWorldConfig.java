package me.jamieclash.parkourworldgen.config;

public final class ParkourWorldConfig {
    public static boolean overworld = true;
    public static boolean nether = true;
    public static boolean end = true;

    public static int overworldGap = 2;
    public static int netherGap = 3;
    public static int endGap = 4;

    public static void resetDefaults() {
        overworld = true;
        nether = true;
        end = true;
        overworldGap = 2;
        netherGap = 3;
        endGap = 4;
    }
}
