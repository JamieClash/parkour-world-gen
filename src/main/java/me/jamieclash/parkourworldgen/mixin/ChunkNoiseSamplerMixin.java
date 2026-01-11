package me.jamieclash.parkourworldgen.mixin;

import me.jamieclash.parkourworldgen.chunk.ParkourNoiseRouterCache;
import me.jamieclash.parkourworldgen.chunk.SparseColumnMaskDensityFunction;
import me.jamieclash.parkourworldgen.config.FrozenParkourWorldSettings;
import me.jamieclash.parkourworldgen.config.ParkourWorldConfig;
import me.jamieclash.parkourworldgen.world.ParkourWorldSavedData;
import me.jamieclash.parkourworldgen.world.WorldGenContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.WeakHashMap;

@Mixin(ChunkNoiseSampler.class)
public class ChunkNoiseSamplerMixin {
    @Unique
    private static final int NETHER = 1;
    @Unique
    private static final int END = 2;

    @Redirect(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/gen/noise/NoiseConfig;getNoiseRouter()Lnet/minecraft/world/gen/noise/NoiseRouter;"
            )
    )
    private NoiseRouter wrapRouter(
            NoiseConfig noiseConfig
    ) {
        NoiseRouter original = noiseConfig.getNoiseRouter();

        int dim = WorldGenContext.getDim();

        if (dim == -1){
            return original;
        }

        FrozenParkourWorldSettings settings = ParkourWorldConfig.getFrozen();
        if (settings == null){
            return original;
        }

        return ParkourNoiseRouterCache.getOrCreate(
                settings,
                dim,
                () -> createRouter(settings, dim, original)
        );
    }

    @Unique
    private static NoiseRouter createRouter(FrozenParkourWorldSettings settings, int dim, NoiseRouter original){
        int gap;
        boolean cellBased;

        if (dim == END){
            gap = settings.endGap();
            cellBased = false;
        }else if (dim == NETHER){
            gap = settings.netherGap();
            cellBased = false;
        }else{
            gap = settings.overworldGap();
            cellBased = false;
        }

        return new NoiseRouter(
                original.barrierNoise(),
                original.fluidLevelFloodednessNoise(),
                original.fluidLevelSpreadNoise(),
                original.lavaNoise(),
                original.temperature(),
                original.vegetation(),
                original.continents(),
                original.erosion(),
                original.depth(),
                original.ridges(),
                original.preliminarySurfaceLevel(),
                wrap(original.finalDensity(), gap, cellBased),
                original.veinToggle(),
                original.veinRidged(),
                original.veinGap()
        );
    }

    @Unique
    private static DensityFunction wrap(DensityFunction original, int gap, boolean cellBased){
        return new SparseColumnMaskDensityFunction(original, gap, cellBased);
    }
}
