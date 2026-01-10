package me.jamieclash.parkourworldgen.mixin;

import me.jamieclash.parkourworldgen.chunk.SparseColumnMaskDensityFunction;
import me.jamieclash.parkourworldgen.world.ParkourWorldState;
import me.jamieclash.parkourworldgen.world.WorldGenContext;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkNoiseSampler.class)
public class ChunkNoiseSamplerMixin {
    @Unique
    private static final int NETHER = 1;
    @Unique
    private static final int END = 2;

    @Unique
    private static final NoiseRouter[] CACHED_ROUTERS = new NoiseRouter[3];
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

        int dim = WorldGenContext.get();
        return getOrCreateRouter(dim, original);
    }

    @Unique
    private static NoiseRouter getOrCreateRouter(int dim, NoiseRouter original){
        NoiseRouter cached = CACHED_ROUTERS[dim];
        if(cached != null){
            return cached;
        }

        int gap;
        boolean cellBased;

        if (dim == END){
            gap = ParkourWorldState.endGap;
            cellBased = false;
        }else if (dim == NETHER){
            gap = ParkourWorldState.netherGap;
            cellBased = true;
        }else{
            gap = ParkourWorldState.overworldGap;
            cellBased = false;
        }

        NoiseRouter newRouter = new NoiseRouter(
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
                original.initialDensityWithoutJaggedness(),
                wrap(original.finalDensity(), gap, cellBased),
                original.veinToggle(),
                original.veinRidged(),
                original.veinGap()
        );

        CACHED_ROUTERS[dim] = newRouter;
        return newRouter;
    }

    @Unique
    private static DensityFunction wrap(DensityFunction original, int gap, boolean cellBased){
        return new SparseColumnMaskDensityFunction(original, gap, cellBased);
    }
}
