package me.jamieclash.parkourworldgen.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.jamieclash.parkourworldgen.chunk.SparseColumnMaskDensityFunction;
import me.jamieclash.parkourworldgen.world.ParkourWorldState;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
    @Unique
    private static final int OVERWORLD = 0;
    @Unique
    private static final int NETHER = 1;
    @Unique
    private static final int END = 2;

    @Unique
    private static final NoiseRouter[] CACHED_ROUTERS = new NoiseRouter[3];

    @Inject(
            method = "createChunkNoiseSampler",
            at = @At("HEAD")
    )
    private void injectColumnMask(
            Chunk chunk,
            StructureAccessor world,
            Blender blender,
            NoiseConfig noiseConfig,
            CallbackInfoReturnable<ChunkNoiseSampler> cir
    ) {
        NoiseChunkGenerator self = (NoiseChunkGenerator) (Object) this;
        int dim = getDimension(self);

        NoiseRouter router = getOrCreateRouter(dim, noiseConfig.getNoiseRouter());

        try {
            Field routerField = NoiseConfig.class.getDeclaredField("noiseRouter");
            routerField.setAccessible(true);
            routerField.set(noiseConfig, router);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private static int getDimension(NoiseChunkGenerator self){
        BiomeSource source = self.getBiomeSource();
        if(source instanceof TheEndBiomeSource){
            return END;
        }else if (source instanceof MultiNoiseBiomeSource){
            if (!self.getSettings().value().hasAquifers()){
                return NETHER;
            }
            return OVERWORLD;
        }
        return OVERWORLD;
    }

    @Unique
    private static DensityFunction wrap(DensityFunction original, int gap, boolean cellBased){
        return new SparseColumnMaskDensityFunction(original, gap, cellBased);
    }
}
