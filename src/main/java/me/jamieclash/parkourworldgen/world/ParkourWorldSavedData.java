package me.jamieclash.parkourworldgen.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

public class ParkourWorldSavedData extends PersistentState {
    public static final String ID = "parkourworldgen";

    public boolean enableOverworld = true;
    public boolean enableNether = true;
    public boolean enableEnd = true;

    public int overworldGap = 2;
    public int netherGap = 3;
    public int endGap = 4;

    public ParkourWorldSavedData(){}

    public static final Codec<ParkourWorldSavedData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("EnableOverworld", true)
                            .forGetter(d -> d.enableOverworld),
                    Codec.BOOL.optionalFieldOf("EnableNether", true)
                            .forGetter(d -> d.enableNether),
                    Codec.BOOL.optionalFieldOf("EnableEnd", true)
                            .forGetter(d -> d.enableEnd),

                    Codec.INT.optionalFieldOf("OverworldGap", 2)
                            .forGetter(d -> d.overworldGap),
                    Codec.INT.optionalFieldOf("NetherGap", 3)
                            .forGetter(d -> d.netherGap),
                    Codec.INT.optionalFieldOf("EndGap", 4)
                            .forGetter(d -> d.endGap)
            ).apply(instance, (eo, en, ee, og, ng, eg) -> {
                ParkourWorldSavedData data = new ParkourWorldSavedData();
                data.enableOverworld = eo;
                data.enableNether = en;
                data.enableEnd = ee;
                data.overworldGap = og;
                data.netherGap = ng;
                data.endGap = eg;
                return data;
            }));

    public void applyToConfig() {
        ParkourWorldConfig.enableOverworld = this.enableOverworld;
        ParkourWorldConfig.enableNether = this.enableNether;
        ParkourWorldConfig.enableEnd = this.enableEnd;

        ParkourWorldConfig.overworldGap = this.overworldGap;
        ParkourWorldConfig.netherGap = this.netherGap;
        ParkourWorldConfig.endGap = this.endGap;

        ParkourWorldConfig.freeze();
    }

    public static final PersistentStateType<ParkourWorldSavedData> TYPE =
            new PersistentStateType<>(
                    ID,
                    ParkourWorldSavedData::new,
                    CODEC,
                    DataFixTypes.SAVED_DATA_COMMAND_STORAGE
            );

    public static ParkourWorldSavedData get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE);
    }
}
