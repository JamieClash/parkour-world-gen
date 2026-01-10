package me.jamieclash.parkourworldgen.world;

public final class WorldGenContext {
    private static final ThreadLocal<Integer> DIM = new ThreadLocal<>();

    public static void set(int dim) {
        DIM.set(dim);
    }

    public static int get() {
        Integer d = DIM.get();
        return d != null ? d : 0;
    }

    public static void clear() {
        DIM.remove();
    }
}