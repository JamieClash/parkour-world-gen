package me.jamieclash.parkourworldgen.world;

public final class ParkourWorldState {
    public static boolean overworld;
    public static boolean nether;
    public static boolean end;

    public static int overworldGap;
    public static int netherGap;
    public static int endGap;

    public static void set(boolean o, boolean n, boolean e, int og, int ng, int eg) {
        overworld = o;
        nether = n;
        end = e;

        overworldGap = og;
        netherGap = ng;
        endGap = eg;
    }
}
