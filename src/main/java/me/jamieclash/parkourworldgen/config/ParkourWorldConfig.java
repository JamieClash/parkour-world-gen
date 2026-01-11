package me.jamieclash.parkourworldgen.config;

import me.jamieclash.parkourworldgen.world.ParkourWorldSavedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public final class ParkourWorldConfig {
    public static boolean enableOverworld = true;
    public static boolean enableNether = true;
    public static boolean enableEnd = true;

    public static int overworldGap = 2;
    public static int netherGap = 2;
    public static int endGap = 4;

    private static FrozenParkourWorldSettings frozen;

    public static volatile ParkourWorldSavedData DATA = null;

    public static void setSavedData(ServerWorld world){
        MinecraftServer server = world.getServer();

        if (server == null){
            DATA = null;
            return;
        }

        DATA = ParkourWorldSavedData.get(server);
    }

    public static ParkourWorldSavedData getSavedData(){
        return DATA;
    }


    public static void clear(){
        DATA = null;
    }

    public static void clearFrozen(){
        frozen = null;
    }

    public static void freeze(){
        frozen = new FrozenParkourWorldSettings(
                enableOverworld,
                enableNether,
                enableEnd,
                overworldGap,
                netherGap,
                endGap
        );
    }

    public static FrozenParkourWorldSettings getFrozen(){
        return frozen;
    }
}
