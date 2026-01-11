package me.jamieclash.parkourworldgen.world;

public final class ParkourWorldState {
    public static boolean enableOverworld;
    public static boolean enableNether;
    public static boolean enableEnd;

    public static int overworldGap;
    public static int netherGap;
    public static int endGap;

    public static void set(boolean o, boolean n, boolean e, int og, int ng, int eg) {
        enableOverworld = o;
        enableNether = n;
        enableEnd = e;

        overworldGap = og;
        netherGap = ng;
        endGap = eg;
    }
}
